package com.shred.sc.filter;

import com.shred.sc.UserService;
import com.shred.sc.feign.UserFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@Slf4j
public class TokenGlobalFilter implements GlobalFilter, Ordered {

    @Reference
    private UserService userFeignClient;

    /**
     * 进⾏token的验证，⽤户微服务和验证码微服务的请求不过滤（⽹关调⽤下游⽤户微服务的token验证接⼝）
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        // 用户微服务和验证码微服务的请求不过滤
        if (path.startsWith("/api/user") || path.startsWith("/api/code")) {
            log.warn("token filter 放行");

            return chain.filter(exchange);
        }

        // 获取Cookie，token不存在或者用户微服务查询不到重定向到登录页面
        List<HttpCookie> cookies = request.getCookies().get("token");
        if (!CollectionUtils.isEmpty(cookies)) {
            HttpCookie cookie = cookies.get(0);
            String token = cookie.getValue();
            if (!"".equals(userFeignClient.getInfo(token))) {
                log.warn("获取到cookies中的有效token，放行");
                return chain.filter(exchange);
            }
        }
        log.warn("无token，重定向到登陆页面");
        // 返回状态码 303，重定向到登录页面
        response.getHeaders().set(HttpHeaders.LOCATION, "/static/login.html");
        response.setStatusCode(HttpStatus.SEE_OTHER);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

