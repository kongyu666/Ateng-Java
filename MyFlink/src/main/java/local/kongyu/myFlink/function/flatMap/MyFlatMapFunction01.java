package local.kongyu.myFlink.function.flatMap;

import com.alibaba.fastjson2.JSONObject;
import local.kongyu.myFlink.entity.UserInfoEntity;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.util.Collector;

/**
 * FlatMap的空格分割实现类
 *
 * @author 孔余
 * @since 2024-02-29 16:13
 */
public class MyFlatMapFunction01 implements FlatMapFunction<String, UserInfoEntity> {
    @Override
    public void flatMap(String str, Collector<UserInfoEntity> collector) throws Exception {
        UserInfoEntity user = JSONObject.parseObject(str).toJavaObject(UserInfoEntity.class);
        collector.collect(user);
    }
}
