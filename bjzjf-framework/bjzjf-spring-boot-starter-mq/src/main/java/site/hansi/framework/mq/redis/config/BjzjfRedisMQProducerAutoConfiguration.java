package site.hansi.framework.mq.redis.config;

import site.hansi.framework.mq.redis.core.RedisMQTemplate;
import site.hansi.framework.mq.redis.core.interceptor.RedisMessageInterceptor;
import site.hansi.framework.redis.config.BjzjfRedisAutoConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

/**
 * Redis 消息队列 Producer 配置类
 *
 * @author 北京智匠坊
 */
@Slf4j
@AutoConfiguration(after = BjzjfRedisAutoConfiguration.class)
public class BjzjfRedisMQProducerAutoConfiguration {

    @Bean
    public RedisMQTemplate redisMQTemplate(StringRedisTemplate redisTemplate,
                                           List<RedisMessageInterceptor> interceptors) {
        RedisMQTemplate redisMQTemplate = new RedisMQTemplate(redisTemplate);
        // 添加拦截器
        interceptors.forEach(redisMQTemplate::addInterceptor);
        return redisMQTemplate;
    }

}
