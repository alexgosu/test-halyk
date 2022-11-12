package kz.example.halykfinance.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class HazelcastConfig {

    @Bean
    public HazelcastInstance getHazelcastInstance(@Value("${hazelcast.cluster}") String cluster,
                                                  @Value("${hazelcast.addresses}") List<String> addresses) {
        var config = new ClientConfig();
        config.setClusterName(cluster);
        config.getNetworkConfig().setAddresses(addresses);
        return HazelcastClient.newHazelcastClient(config);
    }
}
