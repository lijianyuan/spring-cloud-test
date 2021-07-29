package com.nacos.uri.uri.conf;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.gateway.config.GatewayAutoConfiguration;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: KeyResolver
 * @author: ljy
 * @date: 2021年07月28日 14:03
 * @email 15810874514@163.com
 */

//@Component  编写类测试
public class APIKeyResolver implements KeyResolver {


    @Override
    public Mono<String> resolve(ServerWebExchange exchange) {
        return  Mono.just(exchange.getRequest().getPath().value());
    }
}

    