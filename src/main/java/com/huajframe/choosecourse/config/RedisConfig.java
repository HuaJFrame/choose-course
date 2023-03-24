package com.huajframe.choosecourse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis配置
 *
 * @author Hua JFarmer
 */
@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
        //key序列器
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //value序列器, 动态类型将对象映射到json字符串，而JDK序列化是二进制
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        //Hash类型 key序列器
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //Hash类型 value序列器
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public DefaultRedisScript<Long> redisScript(){
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setLocation(new ClassPathResource("lock.lua"));
        redisScript.setResultType(Long.class);
        return redisScript;
    }
}
