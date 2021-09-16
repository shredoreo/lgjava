package com.shred.sc.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.lang.annotation.Annotation;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class BlackListFilter implements GlobalFilter, Order {
    private static final List<String > blackList = new ArrayList<>();

    static {
        blackList.add("0:0:0:0:0:0:0:1"); // 模拟本机地址
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }

    /**
     * 过滤器核心方法
     * @param exchange 封装了request response
     * @param chain 网关过滤器链
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String clientIp = request.getRemoteAddress().getHostString();

        if (blackList.contains(clientIp)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            log.debug("===>IP:"+ clientIp+" 在黑名单总，将被拒绝访问");

            String s = "Request be denied";
            DataBuffer dataBuffer = response.bufferFactory().wrap(s.getBytes(StandardCharsets.UTF_8));

            return response.writeWith(Mono.just(dataBuffer));
        }

        return chain.filter(exchange);
    }

    /**
     * 优先级
     * @return
     */
    @Override
    public int value() {
        return 0;
    }
}
