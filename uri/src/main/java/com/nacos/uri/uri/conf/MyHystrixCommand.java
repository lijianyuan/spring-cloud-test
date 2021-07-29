package com.nacos.uri.uri.conf;

import com.netflix.hystrix.HystrixCommand;

/**
 * @Description: MyHystrixCommand
 * @author: ljy
 * @date: 2021年07月27日 17:39
 * @email 15810874514@163.com
 */

public class MyHystrixCommand extends HystrixCommand<Object> {
    static int num = 6;
    protected MyHystrixCommand(Setter setter) {
        super(setter);
    }

    @Override
    protected Object run() {
        try {
//                Thread.sleep(200);
            System.out.println("[running]:" + Thread.currentThread());
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            System.out.println("I have been interruptted...");
        }

        return "heihei";
    }

    // 执行失败，包括调用超时的调用逻辑和最大并发量拒绝
    @Override
    protected Object getFallback() {
        System.out.println("[fall back]");
        return "CircuitBreaker fallback:" + num;
    }
}
    