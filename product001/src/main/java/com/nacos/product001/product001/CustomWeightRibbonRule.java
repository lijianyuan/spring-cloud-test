package com.nacos.product001.product001;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: 自定义权重负载
 * @author: ljy
 * @date: 2021年07月28日 16:10
 * @email 15810874514@163.com
 */

//@Component
public class CustomWeightRibbonRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {

    }

    @Override
    public Server choose(Object o) {


        // 获取负载均衡的对象
        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();

        // 获取当前调用的微服务的名称
        String serviceName = baseLoadBalancer.getName();

        // 获取Nocas服务发现的相关组件API
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

        try {
            // 获取一个基于 nacos client 实现权重的负载均衡算法
            Instance instance = namingService.selectOneHealthyInstance(serviceName);

            // 返回一个nacos的server
            return new NacosServer(instance);
        } catch (NacosException e) {

            return null;
        }
    }
}

    