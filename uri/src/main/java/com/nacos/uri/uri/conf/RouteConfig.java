package com.nacos.uri.uri.conf;

/**
 * @Description: RouteConfig
 * @author: ljy
 * @date: 2021年06月28日 15:31
 * @email 15810874514@163.com
 */
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class RouteConfig {

  //  @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes().route(r ->
                r.path("/echo")
                        //转发路由
                        .uri("lb://consum001")
                        //注册自定义过滤器
                        .filters(new AuthFilter())
                        //给定id
                        .id("user-service"))
                .build();
    }
}
