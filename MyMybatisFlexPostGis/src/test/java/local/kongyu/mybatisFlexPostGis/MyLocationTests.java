package local.kongyu.mybatisFlexPostGis;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;
import com.mybatisflex.core.query.QueryWrapper;
import local.kongyu.mybatisFlexPostGis.entity.MyLocationEntity;
import local.kongyu.mybatisFlexPostGis.service.MyLocationService;
import lombok.RequiredArgsConstructor;
import net.postgis.jdbc.geometry.LineString;
import net.postgis.jdbc.geometry.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static local.kongyu.mybatisFlexPostGis.entity.table.MyLocationEntityTableDef.MY_LOCATION_ENTITY;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-16 16:59
 */
@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MyLocationTests {
    private final MyLocationService myLocationService;

    /**
     * 插入一个点
     */
    @Test
    void insert01() {
        MyLocationEntity location = new MyLocationEntity();
        location.setUserId(1L);
        location.setLocation(new Point(106.550483, 29.563707));
        myLocationService.save(location);
    }

    /**
     * 插入一条线
     */
    @Test
    void insert02() {
        MyLocationEntity location = new MyLocationEntity();
        location.setUserId(1L);
        location.setLocation(new LineString(new Point[]{new Point(106.309685, 29.582808), new Point(106.287865, 29.416292)}));
        myLocationService.save(location);
    }

    @Test
    void list01() {
        List<MyLocationEntity> list = myLocationService.list();
        System.out.println(list);
    }

    @Test
    void list02() {
        QueryWrapper wrapper = QueryWrapper.create().select(
                "id",
                "user_id",
                "st_asgeojson(location)::json AS location",
                "create_time"
        );
        List<JSONObject> list = myLocationService.listAs(wrapper, JSONObject.class);
        System.out.println(JSON.toJSONString(list, JSONWriter.Feature.PrettyFormat));
    }

    @Test
    void list03() {
        QueryWrapper wrapper = QueryWrapper.create()
                .select(
                        "json_agg(st_asgeojson(p.*)::json) AS json"
                ).from(MY_LOCATION_ENTITY.as("p"));
        String data = myLocationService.getObjAs(wrapper, String.class);
        System.out.println(JSON.toJSONString(JSONArray.parse(data), JSONWriter.Feature.PrettyFormat));
    }
}
