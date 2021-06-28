package com.nacos.uri.uri;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
//@EnableFeignClients
@EnableHystrix
@EnableCircuitBreaker
@RefreshScope
public class UriApplication {

    public static void main(String[] args) {
        SpringApplication.run(UriApplication.class, args);
    }


}
