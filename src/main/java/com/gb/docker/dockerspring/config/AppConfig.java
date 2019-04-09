package com.gb.docker.dockerspring.config;

import com.gb.docker.dockerspring.model.Transaction;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppConfig {

    @Bean
    public Cache<Integer, Transaction> transactionCache() {
        return CacheBuilder.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS).recordStats().build();
    }
}
