package com.nacos.uri.uri;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Mono;

/**
 * spring boot 注解
 */
@SpringBootApplication

/**
 * nacos 服务发现 注解
 */
@EnableDiscoveryClient
//@EnableFeignClients
/**
 * Hystrix 熔断注解
 */
@EnableHystrix
//@EnableCircuitBreaker EnableHystrix 注解已经包含了 EnableCircuitBreaker 注解
@RefreshScope
public class UriApplication {
    public static void main(String[] args) {
        SpringApplication.run(UriApplication.class, args);
    }




}
