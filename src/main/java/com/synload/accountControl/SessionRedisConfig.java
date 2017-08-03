package com.synload.accountControl;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession
public class SessionRedisConfig {
    @Bean
    public LettuceConnectionFactory connectionFactory() {
        LettuceConnectionFactory lettuce = new LettuceConnectionFactory();
        lettuce.setHostName("redis");
        lettuce.setPort(6379);
        return lettuce;
    }
}
