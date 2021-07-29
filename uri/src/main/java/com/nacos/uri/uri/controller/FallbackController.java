package com.nacos.uri.uri.controller;

/**
 * @Description: FallbackController
 * @author: ljy
 * @date: 2021年06月28日 15:58
 * @email 15810874514@163.com
 */
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {


        /**
         * fallbackCmd熔断处理
         * @return
         */
        @RequestMapping("/fallbackCmd")
        public Mono<String> fallbackCmd() {
        return Mono.just("访问超时，请稍后再试!Cmd");
        }

        /**
         * 全局熔断处理
         * @return
         */
        @RequestMapping("/fallback")
        public Mono<String> fallback() {
        return Mono.just("访问超时，请稍后再试!");
        }




}

    