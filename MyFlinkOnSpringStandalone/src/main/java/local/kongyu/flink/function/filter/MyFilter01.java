package local.kongyu.flink.function.filter;

import com.alibaba.fastjson2.JSONObject;
import local.kongyu.flink.entity.UserInfoEntity;
import org.apache.flink.api.common.functions.FilterFunction;

/**
 * 自定义 FilterFunction，用于过滤符合条件的字符串。
 */
public class MyFilter01 implements FilterFunction<String> {

    /**
     * 判断字符串是否符合条件。
     *
     * @param str 要过滤的字符串
     * @return 如果字符串符合条件返回 true，否则返回 false
     * @throws Exception 可能抛出的异常
     */
    @Override
    public boolean filter(String str) throws Exception {
        // 将 JSON 字符串解析为 JSONObject，然后转换为 Java 对象 UserInfoEntity
        UserInfoEntity user = JSONObject.parseObject(str).toJavaObject(UserInfoEntity.class);

        // 如果用户年龄为 24，返回 true；否则返回 false
        if (user.getAge() == 24) {
            return true;
        }
        return false;
    }
}
