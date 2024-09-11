package local.kongyu.flink.function.map;

import com.alibaba.fastjson2.JSONObject;
import local.kongyu.flink.entity.UserInfoEntity;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;


public class MyMapFunction03 implements MapFunction<String, Tuple2<String, Double>> {

    @Override
    public Tuple2<String, Double> map(String str) throws Exception {
        UserInfoEntity user = JSONObject.parseObject(str).toJavaObject(UserInfoEntity.class);
        return new Tuple2<String, Double>(user.getProvince(), user.getScore());
    }
}
