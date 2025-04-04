package com.test.microblogging.config;

/*import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import com.test.microblogging.utils.Constantes;

import static java.time.Duration.ofSeconds;
import static org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig;

@EnableCaching
@Configuration*/
public class CacheConfig {

    /*@Value("${spring.cache.ttl}")
    private int ttl;
    @Value("${spring.cache.promise-ttl}")
    private int promiseTtl;

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        return defaultCacheConfig()
                .entryTtl(ofSeconds(ttl));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder ->
                builder.withCacheConfiguration(Constantes.TOKEN_CACHE, defaultCacheConfig().entryTtl(ofSeconds(promiseTtl)));
    }*/
}