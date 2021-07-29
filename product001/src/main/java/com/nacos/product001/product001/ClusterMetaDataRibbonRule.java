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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 自定义元数据策率
 * @author: ljy
 * @date: 2021年07月28日 16:55
 * @email 15810874514@163.com
 */
@Component
public class ClusterMetaDataRibbonRule extends AbstractLoadBalancerRule {

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

    @Override
    public void initWithNiwsConfig(IClientConfig iClientConfig) {
    }

    @Override
    public Server choose(Object o) {

        // 获取当前服务的集群名称
        String currentClusterName = nacosDiscoveryProperties.getClusterName();

        // 获取当前版本
        String currentVersion = nacosDiscoveryProperties.getMetadata().get("ver");

        // 获取被调用的服务的名称
        BaseLoadBalancer baseLoadBalancer = (BaseLoadBalancer) getLoadBalancer();
        String serviceName = baseLoadBalancer.getName();

        // 获取nacos clinet的服务注册发现组件的api
        NamingService namingService = nacosDiscoveryProperties.namingServiceInstance();

        try {
            // 获取所有被调用服务
            List<Instance> allInstances = namingService.getAllInstances(serviceName);

            // 过滤出相同版本且相同集群下的所有服务
            List<Instance> sameVersionAndClusterInstances = allInstances.stream()
                    .filter(x -> StringUtils.equalsIgnoreCase(x.getMetadata().get("ver"), currentVersion)
                            && StringUtils.equalsIgnoreCase(x.getClusterName(), currentClusterName)
                    ).collect(Collectors.toList());

            Instance chooseInstance;
            if(sameVersionAndClusterInstances.isEmpty()) {
                // 过滤出所有相同版本的服务
                List<Instance> sameVersionInstances = allInstances.stream()
                        .filter(x -> StringUtils.equalsIgnoreCase(x.getMetadata().get("ver"), currentVersion))
                        .collect(Collectors.toList());
                if(sameVersionInstances.isEmpty()) {
                    throw new RuntimeException("找不到相同版本的微服务实例");
                }
                else {
                    // 随机权重
                    chooseInstance = ExtendBalancer.getHostByRandomWeightCopy(sameVersionInstances);
                    System.out.println("跨集群同版本调用--->当前微服务所在集群:{"+currentClusterName+"},被调用微服务所在集群:{"+chooseInstance.getClusterName()+"}," +
                                    "当前微服务的版本:{"+chooseInstance.getMetadata().get("ver")+"},被调用微服务版本:{"+chooseInstance.getMetadata().get("ver")+"}," +
                                    "Host:{"+chooseInstance.getIp()+"},Port:{"+chooseInstance.getPort()+"}" );
                }
            }
            else {
                chooseInstance = ExtendBalancer.getHostByRandomWeightCopy(sameVersionAndClusterInstances);
                System.out.println("同集群同版本调用--->当前微服务所在集群:{"+currentClusterName+"},被调用微服务所在集群:{"+ chooseInstance.getClusterName()+"}" +
                                ",当前微服务的版本:{"+chooseInstance.getMetadata().get("ver")+"},被调用微服务版本:{"+chooseInstance.getMetadata().get("ver")+"}" +
                                ",Host:{"+chooseInstance.getIp()+"},Port:{"+chooseInstance.getPort()+"}");
            }
            return new NacosServer(chooseInstance);
        } catch (NacosException e) {

            return null;
        }
    }
}
    