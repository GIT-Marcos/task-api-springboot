package com.usuario.todolist.config;

import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.configuration.MutableConfiguration;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

@Configuration
public class CacheConfig {

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            MutableConfiguration<Object, Object> config = new MutableConfiguration<>()
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(Duration.ONE_HOUR))
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true);

            cm.createCache("buckets", config);
        };
    }
}
