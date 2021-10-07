package com.shred.sc.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.google.common.collect.Lists;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@RefreshScope
public class IpFlushGatewayFilterFactory extends AbstractGatewayFilterFactory<IpFlushGatewayFilterFactory.Config> {

    /**
     * 时长，单位分钟
     */
    @Value("${ip.period}")
    private Integer period;

    /**
     * 最大访问次数
     */
    @Value("${ip.limit}")
    private Long limit;

    @Value("${ip.uri}")
    private String uri;

    private static final ConcurrentHashMap<String, List<Long>> ipCounts = new ConcurrentHashMap<>();

    public IpFlushGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public List<String> shortcutFieldOrder() {

        return Lists.newArrayList("tokenName");
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
//            System.out.println("config.tokenName:" + config.uri);
            ServerHttpRequest request = exchange.getRequest();
            ServerHttpResponse response = exchange.getResponse();
            //客户端IP
            String ip = request.getRemoteAddress().getHostName();

            String path = request.getURI().getPath();
            log.warn("path:" + path);
            if (!path.startsWith(uri)) {
                log.warn("不需要拦截ip防暴");

                return chain.filter(exchange);
            }

            List<Long> visitTs = ipCounts.computeIfAbsent(ip, k -> new ArrayList<>());
            visitTs.add(System.currentTimeMillis());

            //
            long startTime = System.currentTimeMillis() - period * 60 * 1000;
            long count = visitTs.stream().filter(s -> s - startTime > 0).count();

            log.warn("" + ip + "==> 最近分钟:" + period + " 访问次数：" + count);

            //是否超过访问次数
            boolean isViolent = count - limit > 0;

            if (isViolent) {
                response.setStatusCode(HttpStatus.FORBIDDEN);
                String msg = ip + " 超过次数，被拒绝访问 ";
                log.warn(msg);

                DataBuffer wrap = response.bufferFactory().wrap(msg.getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(wrap));

            } else {
                return chain.filter(exchange);
            }

        };
    }

    @Data
    public static class Config {
        private String uri;
    }
}