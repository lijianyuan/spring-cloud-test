package com.nacos.uri.uri.conf;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: MyGloablFilter  自定义全局拦截器
 * @author: ljy
 * @date: 2021年07月26日 17:35
 * @email 15810874514@163.com
 */

@Component
public class MyGloablFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        System.out.println("进去到全局 过滤器");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}


    