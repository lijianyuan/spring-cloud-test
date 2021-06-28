package com.nacos.product001.product001;

/**
 * @Description: VodClientImpl
 * @author: ljy
 * @date: 2021年06月24日 15:07
 * @email 15810874514@163.com
 */
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VodClientImpl implements TestFeignClient {

    @Override
    public String echo(String string) {
        return "超时了1111111111111111";
    }
}

    