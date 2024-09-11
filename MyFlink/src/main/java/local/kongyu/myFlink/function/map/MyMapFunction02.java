package local.kongyu.myFlink.function.map;

import com.alibaba.fastjson2.JSONObject;
import local.kongyu.myFlink.entity.UserInfoEntity;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;


public class MyMapFunction02 implements MapFunction<String, Tuple2<String, Long>> {

    @Override
    public Tuple2<String, Long> map(String str) throws Exception {
        UserInfoEntity user = JSONObject.parseObject(str).toJavaObject(UserInfoEntity.class);
        return new Tuple2<String, Long>(user.getProvince(), 1L);
    }
}
