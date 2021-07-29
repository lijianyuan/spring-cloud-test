package com.nacos.uri.uri.conf;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * @Description: MyConfigration  配置类 根据ip限流
 * @author: ljy
 * @date: 2021年07月28日 11:57
 * @email 15810874514@163.com
 */
@Configuration
public class HostAddrKeyResolverConf {


    @Bean("hostAddrKeyResolver") // 根据ip限流
    public KeyResolver hostAddrKeyResolver() {
      /*  return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getHostName()
                +exchange.getRequest().getMethod()+exchange.getRequest().getQueryParams().getFirst("userid")

        );
*/
        return exchange -> Mono.just(
                exchange.getRequest().getRemoteAddress().getHostName()
        );
    }
/*
    @Bean("userKeyResolver") // 根据用户id 默认去参数中的userId
    public KeyResolver userKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("userId"));
    }


    @Bean("apiKeyResolver") // 根据接口地址
    KeyResolver apiKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getPath().value());
    }*/


}

    