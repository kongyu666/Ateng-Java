package local.kongyu.mybatisFlex;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.log.StaticLog;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mybatisflex.core.row.Db;
import com.mybatisflex.core.row.Row;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-18 15:33
 */
@SpringBootTest
public class MyTests {

    @Test
    void test01_3() throws IOException {
        BufferedReader reader = FileUtil.getReader("D:\\Temp\\202401\\GD_AdcodeRoad\\GD_AdcodeRoadState_topic.json", StandardCharsets.UTF_8);
        String line;
        // 获取一行数据
        while ((line = reader.readLine()) != null) {
            try {
                JSONObject lineJson = JSONObject.parseObject(line);
                String name = lineJson.getString("name");
                long utcSeconds = lineJson.getLongValue("utcSeconds");
                String formattedTime = DateUtil.format(DateUtil.date(utcSeconds * 1000L), "yyyy-MM-dd HH:mm:ss");
                JSONArray linkStates = lineJson.getJSONArray("linkStates");
                HashSet<JSONObject> set = new HashSet<>();
                for (int i = 0; i < linkStates.size(); i++) {
                    JSONObject jsonObject = linkStates.getJSONObject(i);
                    String linkId = jsonObject.getString("linkId");
                    //jsonObject.put("name", name);
                    //jsonObject.put("dateTime", formattedTime);
                    JSONObject json = new JSONObject();
                    json.put("link_id", linkId);
                    json.put("date_time", formattedTime);
                    set.add(json);
                }
                List<Row> list = set.stream().map(d -> d.toJavaObject(Row.class)).collect(Collectors.toList());
                //mongoTemplate.insert(set, "zhsq_regional_gd_adcode_road_state_topic_distinct");
                Db.insertBatch("adcode_road_state",list,2000);
                StaticLog.info("数据处理成功");
            } catch (Exception e) {
                StaticLog.error(e, "数据处理错误");
            }
        }
    }
}
