package com.nacos.product001.product001;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.ribbon.NacosServer;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.core.Balancer;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractLoadBalancerRule;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 同一个集群优先调用
 * @author: ljy
 * @date: 2021年07月28日 16:34
 * @email 15810874514@163.com
 */
//@Component
public class CustomClusterRibbonRule extends AbstractLoadBalancerRule {
    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {


        // 获取当前服务所在的集群
        String currentClusterName = nacosDiscoveryProperties.getClusterName();

        // 获取当前调用的微服务的名称
        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) this.getLoadBalancer();
        String serviceName = baseLoadBalancer.getName();

        // 获取nacos client的服务注册发现组件的api
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

        try {
            // 获取多有的服务实例
            List<Instance> allInstances = namingService.getAllInstances(serviceName, true);

            // 获取同一集群下的所有被调用的服务
            List<Instance> sameClusterNameInstList =  allInstances.stream()
                    .filter(instance -> StringUtils.equalsIgnoreCase(instance.getClusterName(), currentClusterName))
                    .collect(Collectors.toList());

            Instance chooseInstance;
            if(sameClusterNameInstList.isEmpty()) {
                // 根据权重随机选择一个
                chooseInstance = ExtendBalancer.getHostByRandomWeightCopy(allInstances);
                System.out.println("发生跨集群调用--->当前微服务所在集群:{"+currentClusterName+"},被调用微服务所在集群:{"+chooseInstance.getClusterName()+"}," +
                                "Host:{"+chooseInstance.getIp()+"},Port:{"+chooseInstance.getPort()+"}");
            }
            else {
                chooseInstance = ExtendBalancer.getHostByRandomWeightCopy(sameClusterNameInstList);
                System.out.println("同集群调用--->当前微服务所在集群:{"+currentClusterName+"},被调用微服务所在集群:{"+chooseInstance.getClusterName()+"},Host:{"+ chooseInstance.getIp()+"}," +
                                "Port:{"+chooseInstance.getPort()+"}" );
            }

            return new NacosServer(chooseInstance);
        } catch (NacosException e) {

            return null;
        }
    }
}

class ExtendBalancer extends Balancer {
    /**
     * 根据权重选择随机选择一个
     */
    public static Instance getHostByRandomWeightCopy(List<Instance> hosts) {
        return getHostByRandomWeight(hosts);
    }
}
    