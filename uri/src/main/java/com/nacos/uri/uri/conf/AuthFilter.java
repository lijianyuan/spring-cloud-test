package com.nacos.uri.uri.conf;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: AuthFilter  鉴权拦截器
 * @author: ljy
 * @date: 2021年07月26日 17:15
 * @email 15810874514@163.com
 */


public class AuthFilter implements GatewayFilter, Ordered {

    private static final String AUTH = "Authorization";
    private static final String USERNAME = "demo-user-name";



    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //  ServerWebExchange 可以操作 getRrequest   getRresponse  getSession  exchange.getAttributes exchange.getRequest().getCookies 等等操作
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders header = request.getHeaders();
        String token = header.getFirst(AUTH);
        String username = header.getFirst(USERNAME);
        System.out.println("%%%%%%%%%%%%%%%%%%%%%%%%进入到拦截器里面");
        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isBlank(token)) {

            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }

        Map<String,Object> resp = new HashMap<String,Object>();//此处调用方法  获取用户信息进行鉴权逻辑  authService.verify(token, username);
        resp.put("code",200);
        if (Integer.valueOf(resp.get("code").toString()) != 200) { //模拟鉴权失败
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }

        // TODO 将用户信息存放在请求header中传递给下游业务
        ServerHttpRequest.Builder mutate = request.mutate();
        mutate.header("demo-user-name", username);
        ServerHttpRequest buildReuqest = mutate.build();

        //todo 如果响应中需要放数据，也可以放在response的header中
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().add("demo-user-name",username);
        return chain.filter(exchange.mutate()
                .request(buildReuqest)
                .response(response)
                .build());
    }

    ///    *过滤器存在优先级，order越大，优先级越低
    @Override
    public int getOrder() {
        return 0;
    }
}

    