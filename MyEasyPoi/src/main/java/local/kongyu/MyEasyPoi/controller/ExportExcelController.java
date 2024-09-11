package local.kongyu.MyEasyPoi.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.github.javafaker.Faker;
import local.kongyu.MyEasyPoi.entity.UserInfoEntity;
import local.kongyu.MyEasyPoi.utils.EasyPoiExcelUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 导出Excel数据
 *
 * @author 孔余
 * @since 2024-02-01 22:30
 */
@RestController
@RequestMapping("/export")
public class ExportExcelController {
    /**
     * 根据实体类导出Excel数据
     *
     * @param response
     */
    @GetMapping("/excel-user")
    public void excelUser(HttpServletResponse response) {
        List<UserInfoEntity> list = initUserInfoData();
        EasyPoiExcelUtil.exportExcel(
                list,
                "用户信息",
                "用户信息",
                UserInfoEntity.class,
                "用户信息",
                response
        );
    }

    /**
     * 根据自定义表头和对应的数据导出Excel
     *
     * @param response
     */
    @GetMapping("/excel-custom")
    public void excelCustom(@RequestParam(required = false, defaultValue = "10") Integer count, HttpServletResponse response) {
        JSONObject json = initCustomData(count.intValue());
        // 表头
        Map<Object, String> headMap = new HashMap<>();
        headMap = json.getJSONObject("headMap").toJavaObject(Map.class);
        // 数据
        List<Map<String, Object>> dataList = new ArrayList<>();
        dataList = JSON.parseArray(JSON.toJSONString(json.getJSONArray("dataList")), (Type) Map.class);
        /*EasyPoiExcelUtil.exportExcel(
                headMap,
                dataList,
                "用户信息",
                "用户信息",
                "用户信息",
                response
        );*/
    }


    // 生成测试数据
    private List<UserInfoEntity> initUserInfoData() {
        // 创建一个Java Faker实例，指定Locale为中文
        Faker faker = new Faker(new Locale("zh-CN"));
        // 创建一个包含不少于100条JSON数据的列表
        List<UserInfoEntity> userList = new ArrayList();
        for (int i = 1; i <= 3000; i++) {
            UserInfoEntity user = new UserInfoEntity();
            user.setId((long) i);
            user.setName(faker.name().fullName());
            user.setBirthday(faker.date().birthday());
            user.setAge(faker.number().numberBetween(0, 100));
            user.setProvince(faker.address().state());
            user.setCity(faker.address().cityName());
            user.setScore(faker.number().randomDouble(3, 1, 100));
            userList.add(user);
        }
        return userList;
    }

    private JSONObject initCustomData(int count) {
        DateTime dateTime = DateUtil.date();
        // 表头
        Map<Object, String> headMap = new LinkedHashMap<>();
        // 数据
        List<Map<String, Object>> dataList = new ArrayList<>();
        // 创建一个Java Faker实例，指定Locale为中文
        Faker faker = new Faker(new Locale("zh-CN"));
        // 表头
        headMap.put("id", "用户ID");
        headMap.put("name", "用户名");
        for (int i = 1; i <= count; i++) {
            String dateTimeStr = DateUtil.format(dateTime, "yyyy-MM-dd");
            headMap.put(dateTimeStr, StrUtil.format("xxx{}xxx", dateTimeStr));
            dateTime = DateUtil.offsetDay(dateTime, 1);
        }
        // 数据
        for (int i = 0; i < count * 10; i++) {
            HashMap<String, Object> map = new HashMap<>();
            headMap.keySet().forEach(key -> {
                if ("id".equals(key)) {
                    map.put("id", IdUtil.fastUUID());
                } else if ("name".equals(key)) {
                    map.put("name", faker.name().fullName());
                } else {
                    map.put(key.toString(), faker.number().randomDouble(3, 1, 100));
                }
            });
            dataList.add(map);
        }
        return JSONObject.of("headMap", headMap, "dataList", dataList);
    }
}
