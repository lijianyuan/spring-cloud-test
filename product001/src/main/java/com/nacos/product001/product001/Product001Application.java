package com.nacos.product001.product001;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**springboot注解*/
@SpringBootApplication
/**nacos 注册注解*/
@EnableDiscoveryClient
/** Feign 注解 也可以采用 RestTemplate 方式**/
@EnableFeignClients
/**Hystrix 熔断注解 EnableHystrix注解包含了EnableCircuitBreaker*/
@EnableHystrix
//@EnableCircuitBreaker  Hystrix 熔断注解 EnableCircuitBreaker EnableHystrix 二者选一个就行
@RefreshScope
public class Product001Application {

    public static void main(String[] args) {
        SpringApplication.run(Product001Application.class, args);
    }
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @RestController
    public class TestController {


        private final RestTemplate restTemplate;

        @Autowired
        public TestController(RestTemplate restTemplate) {this.restTemplate = restTemplate;}

        @Autowired
        private TestFeignClient testFeignClient;

        @RequestMapping(value = "/echo/{str}", method = RequestMethod.GET)
        public String echo(@PathVariable String str) {
            //return restTemplate.getForObject("http://consum001/echo/" + str, String.class);
            return  testFeignClient.echo(str);
        }
        @RequestMapping(value = "/test0001/{str}", method = RequestMethod.GET)
        public String test0001(@PathVariable String str) {
            //return restTemplate.getForObject("http://consum001/echo/" + str, String.class);
            return  testFeignClient.echo(str);
        }

    }

}
