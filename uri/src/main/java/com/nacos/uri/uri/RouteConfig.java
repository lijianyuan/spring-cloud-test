package com.nacos.uri.uri;

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
        return builder.routes()
                .route("echo", r -> r.path("/echo/**")
                         .filters(v->v.stripPrefix(1))
                        .uri("lb://consum001"))
                .build();
    }
}
