package local.kongyu.redisTemplate.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableConfigurationProperties({MyRedisProperties.class})
public class MyStringRedisTemplateConfig {
    private MyRedisProperties myRedisProperties;

    @Autowired
    public MyStringRedisTemplateConfig(MyRedisProperties myRedisProperties) {
        this.myRedisProperties = myRedisProperties;
    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return getRedisTemplate(redisConnectionFactory);
    }

    /**
     * 在 Spring Data Redis 中集成 Fastjson2
     * 使用 GenericFastJsonRedisSerializer 作为 RedisTemplate 的 RedisSerializer 来提升JSON序列化和反序列化速度。
     * https://github.com/alibaba/fastjson2/blob/main/docs/spring_support_cn.md#4-%E5%9C%A8-spring-data-redis-%E4%B8%AD%E9%9B%86%E6%88%90-fastjson2
     * @param redisConnectionFactory
     * @return
     */
    private RedisTemplate getRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        /**
         * 使用GenericFastJsonRedisSerializer来序列化和反序列化redis的key和value值
         */
        //GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        //redisTemplate.setDefaultSerializer(fastJsonRedisSerializer);//设置默认的Serialize，包含 keySerializer & valueSerializer

        /**
         * 使用StringRedisSerializer来序列化和反序列化redis的key值
         * 使用FastJsonRedisSerializer来序列化和反序列化redis的value值
         */
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        MyFastJsonRedisSerializer myFastJsonRedisSerializer = new MyFastJsonRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
        redisTemplate.setValueSerializer(myFastJsonRedisSerializer);
        redisTemplate.setValueSerializer(myFastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(myFastJsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * 自定义Redis配置，从MyRedisProperties获取redis-dev的配置，stringRedisTemplateDev
     * 使用：
     * @Qualifier("stringRedisTemplateDev")
     * private final StringRedisTemplate stringRedisTemplateDev;
     * @return
     */
    @Bean
    public StringRedisTemplate stringRedisTemplateDev() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(this.createLettuceConnectionFactory(myRedisProperties.getRedisDev()));
        return stringRedisTemplate;
    }
    /**
     * 自定义Redis配置，从MyRedisProperties获取redis-dev的配置，redisTemplateDev
     * 使用：
     * @Qualifier("redisTemplateDev")
     * private final RedisTemplate redisTemplateDev;
     * @return
     */
    @Bean
    public RedisTemplate redisTemplateDev() {
        LettuceConnectionFactory factory = this.createLettuceConnectionFactory(myRedisProperties.getRedisDev());
        return getRedisTemplate(factory);
    }

    /**
     * Lettuce的Redis连接工厂
     * @param redisProperties Redis服务的参数
     * @return Lettuce连接工厂
     */
    private LettuceConnectionFactory createLettuceConnectionFactory(RedisProperties redisProperties) {
        RedisProperties.Pool pool = redisProperties.getLettuce().getPool();
        // 设置Redis的服务参数
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration();
        redisConfig.setHostName(redisProperties.getHost());
        redisConfig.setDatabase(redisProperties.getDatabase());
        redisConfig.setPort(redisProperties.getPort());
        redisConfig.setPassword(redisProperties.getPassword());
        // 设置连接池属性
        GenericObjectPoolConfig<Object> poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxTotal(pool.getMaxActive());
        poolConfig.setMaxIdle(pool.getMaxIdle());
        poolConfig.setMinIdle(pool.getMinIdle());
        poolConfig.setMaxWait(pool.getMaxWait());
        poolConfig.setTimeBetweenEvictionRuns(pool.getTimeBetweenEvictionRuns());
        LettucePoolingClientConfiguration clientConfig = LettucePoolingClientConfiguration
                .builder()
                .commandTimeout(redisProperties.getTimeout())
                .poolConfig(poolConfig)
                .build();
        // 返回Lettuce的Redis连接工厂
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisConfig, clientConfig);
        factory.afterPropertiesSet();
        return factory;
    }

}