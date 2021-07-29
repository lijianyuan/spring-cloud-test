package com.nacos.uri.uri.conf;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

/**
 * @Description: 信号量隔离
 * @author: ljy
 * @date: 2021年07月27日 17:38
 * @email 15810874514@163.com
 *
 * 测试：同一GroupKey，不同CommandKey对应的信号量是否相同
 * 结果：不同的CommandKey对应独立的信号量。
 *
 */


public class TestSemaphoreIsolation {
    static int num = 6;

    public static void main(String[] args) {
        // 同一个CommandKey，公用一个信号量
        HystrixCommand.Setter setter = HystrixCommand.Setter
                .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestSemaphoreGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("TestSemaphoreCommand"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withCircuitBreakerEnabled(true)
                        .withExecutionTimeoutEnabled(true)
                        .withExecutionTimeoutInMilliseconds(3000)
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(num));


        for (int i = 0; i < num + 1; i++) {
            final int temp = i;
            // 一个CommandKey一个信号量和Group没得关系了
//            HystrixCommand.Setter setter = HystrixCommand.Setter
//                    .withGroupKey(HystrixCommandGroupKey.Factory.asKey("TestSemaphoreGroup"))
//                    .andCommandKey(HystrixCommandKey.Factory.asKey("TestSemaphoreCommand" + temp))
//                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
//                            .withCircuitBreakerEnabled(true)
//                            .withExecutionTimeoutEnabled(true)
//                            .withExecutionTimeoutInMilliseconds(3000)
//                            .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
//                            .withExecutionIsolationSemaphoreMaxConcurrentRequests(num));

            // 一个command只能执行一次，每次执行调用
            MyHystrixCommand hystrixCommand = new MyHystrixCommand(setter);
            new Thread(() -> {
                System.out.println("i = " + temp);
                // 同步调用  里面是异步调用加上futrue.get，所以具体的执行不是在当前的线程中
                String result = (String) hystrixCommand.execute();
                System.out.println("i = " + temp + ", result = " + result);
            }).start();
        }
    }


}

    