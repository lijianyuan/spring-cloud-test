package com.nacos.product001.product001;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *@Description: TestFeignClient
 *@Param: 
 *@return: 
 *@Author: ljy
 *@Date: 2021/6/24 13:59
 *@email: 15810874514@163.com
 *
 **/
//@FeignClient("")
@FeignClient(value = "consum001", fallback  = VodClientImpl.class)
@Component
public interface TestFeignClient {

    @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
    public String echo(@PathVariable(value = "string") String string);



}
