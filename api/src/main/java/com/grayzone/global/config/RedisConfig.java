package com.grayzone.global.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String redisHost;

  @Value("${spring.data.redis.port}")
  private int redisPort;

  @Value("${spring.data.redis.password}")
  private String password;

  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(
      redisHost, redisPort);
    redisStandaloneConfiguration.setPassword(password);

    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
    connectionFactory.setValidateConnection(true);
    connectionFactory.afterPropertiesSet();

    return connectionFactory;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory connectionFactory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.activateDefaultTyping(
      objectMapper.getPolymorphicTypeValidator(),
      ObjectMapper.DefaultTyping.NON_FINAL
    );

    objectMapper.configure(DeserializationFeature.USE_LONG_FOR_INTS, true);

    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

    template.setValueSerializer(serializer);
    template.setHashValueSerializer(serializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setKeySerializer(new StringRedisSerializer());
    template.afterPropertiesSet();

    return template;
  }

}
