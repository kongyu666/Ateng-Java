package local.kongyu.redisTemplate.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.ContextAutoTypeBeforeHandler;
import com.alibaba.fastjson2.filter.Filter;
import com.alibaba.fastjson2.support.config.FastJsonConfig;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

/**
 * RedisTemplate Fastjson2 Serializer
 * 自定义Redis序列化
 * @author 孔余
 * @since 2024-01-30 17:29
 */
public class MyFastJsonRedisSerializer implements RedisSerializer<Object> {
    private final FastJsonConfig config = new FastJsonConfig();
    private static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(
            // 按需加上需要支持自动类型的类名前缀，范围越小越安全
            "local.", "com."
    );

    public MyFastJsonRedisSerializer() {
        //config.setReaderFeatures(JSONReader.Feature.SupportAutoType);
        config.setWriterFeatures(
                JSONWriter.Feature.WriteClassName,
                JSONWriter.Feature.NotWriteNumberClassName,
                JSONWriter.Feature.NotWriteSetClassName,
                JSONWriter.Feature.WriteNulls,
                //JSONWriter.Feature.WriteLongAsString,
                JSONWriter.Feature.NullAsDefaultValue
        );
        config.setDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }

    public MyFastJsonRedisSerializer(String[] acceptNames, boolean jsonb) {
        this();
        config.setReaderFilters(new ContextAutoTypeBeforeHandler(acceptNames));
        config.setJSONB(jsonb);
    }

    public MyFastJsonRedisSerializer(String[] acceptNames) {
        this(acceptNames, false);
    }

    public MyFastJsonRedisSerializer(boolean jsonb) {
        this(new String[0], jsonb);
    }

    @Override
    public byte[] serialize(Object object) throws SerializationException {
        if (object == null) {
            return new byte[0];
        }
        try {
            if (config.isJSONB()) {
                return JSONB.toBytes(object, config.getWriterFeatures());
            } else {
                return JSON.toJSONBytes(object, config.getWriterFeatures());
            }
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            if (config.isJSONB()) {
                return JSONB.parseObject(bytes, Object.class, AUTO_TYPE_FILTER, config.getReaderFeatures());
            } else {
                return JSON.parseObject(bytes, Object.class, AUTO_TYPE_FILTER, config.getReaderFeatures());
            }
        } catch (Exception ex) {
            throw new SerializationException("Could not deserialize: " + ex.getMessage(), ex);
        }
    }
}
