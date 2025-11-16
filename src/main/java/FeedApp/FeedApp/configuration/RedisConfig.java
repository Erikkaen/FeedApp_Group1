package FeedApp.FeedApp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.UnifiedJedis;

@Configuration
public class RedisConfig {

    @Bean
    public UnifiedJedis jedisClient() {
        return new UnifiedJedis("redis://redis:6379");
    }
}
