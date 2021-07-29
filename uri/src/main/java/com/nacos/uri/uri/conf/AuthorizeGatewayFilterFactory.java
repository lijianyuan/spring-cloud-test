package com.nacos.uri.uri.conf;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @Description: AuthorizeGatewayFilterFactory
 * @author: ljy
 * @date: 2021年07月26日 17:37
 * @email 15810874514@163.com
 *
 * 很多时候我们更希望在配置文件中配置Gateway Filter,所以我们可以自定义过滤器工厂实现。
 * 自定义过滤器工厂需要继承AbstractGatewayFilterFactory
 */

@Component
public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {



    private static final String AUTHORIZE_TOKEN = "token";
    private static final String AUTHORIZE_UID = "uid";



    public AuthorizeGatewayFilterFactory() {
        super(Config.class);
        System.out.println("Loaded GatewayFilterFactory [Authorize]");
    }

    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("enabled");
    }

    @Override
    public GatewayFilter apply(AuthorizeGatewayFilterFactory.Config config) {
        return (exchange, chain) -> {
            if (!config.isEnabled()) {
                return chain.filter(exchange);
            }
            System.out.println("进入到 过滤器工厂！");
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();
            String token = headers.getFirst(AUTHORIZE_TOKEN);
            String uid = headers.getFirst(AUTHORIZE_UID);
            if (token == null) {
                token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
            }
            if (uid == null) {
                uid = request.getQueryParams().getFirst(AUTHORIZE_UID);
            }

            ServerHttpResponse response = exchange.getResponse();
            if (StringUtils.isEmpty(token) || StringUtils.isEmpty(uid)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            String authToken =""; //stringRedisTemplate.opsForValue().get(uid);
            if (authToken == null || !authToken.equals(token)) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
        // 控制是否开启认证
        private boolean enabled;

        public Config() {}

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }
}


    