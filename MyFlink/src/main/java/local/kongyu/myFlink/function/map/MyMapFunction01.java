package local.kongyu.myFlink.function.map;

import com.alibaba.fastjson2.JSONObject;
import local.kongyu.myFlink.entity.UserInfoEntity;
import org.apache.flink.api.common.functions.MapFunction;

/**
 * 自定义 MapFunction，用于将 JSON 字符串映射为 UserInfoEntity 对象。
 */
public class MyMapFunction01 implements MapFunction<String, UserInfoEntity> {

    /**
     * 将 JSON 字符串映射为 UserInfoEntity 对象。
     * @param str JSON 字符串
     * @return UserInfoEntity 对象
     * @throws Exception 可能抛出的异常
     */
    @Override
    public UserInfoEntity map(String str) throws Exception {
        // 将 JSON 字符串解析为 JSONObject，然后转换为 Java 对象 UserInfoEntity
        return JSONObject.parseObject(str).toJavaObject(UserInfoEntity.class);
    }
}
