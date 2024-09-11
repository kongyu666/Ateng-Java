package local.kongyu.MyEasyPoi.controller;

import cn.afterturn.easypoi.entity.ImageEntity;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import com.github.javafaker.Faker;
import local.kongyu.MyEasyPoi.entity.UserInfoEntity;
import local.kongyu.MyEasyPoi.utils.EasyPoiExcelUtil;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 导出Excel数据
 *
 * @author 孔余
 * @since 2024-02-01 22:30
 */
@RestController
@RequestMapping("/template")
public class ExportTemplateController {

    @GetMapping("/image")
    public void image(HttpServletResponse response) {
        // 构建数据，对应模板中的{{xx}}，例如map中的name对应模板中的{{name}}
        HashMap<String, Object> map = new HashMap<>();
        ImageEntity image = new ImageEntity();
        image.setColspan(3); // 向右合并3列
        image.setRowspan(10); // 向下合并4行
        image.setType("data");
        image.setData(HttpUtil.downloadBytes("http://192.168.1.12:9000/data/image/logo1.jpg"));
        map.put("image", image);
        map.put("name", "阿腾");
        // 导出数据
        TemplateExportParams exportParams = new TemplateExportParams(new ClassPathResource("/templates/image.xlsx").getPath(), "image");
        EasyPoiExcelUtil.exportTemplateExcel(exportParams, map, "image", response);
    }

    @GetMapping("/list")
    public void list(HttpServletResponse response) {
        // 构建数据，对应模板中的{{xx}}，例如map中的name对应模板中的{{name}}
        HashMap<String, Object> map = new HashMap<>();
        //List<HashMap> collect = initUserInfoData().stream().map(user -> JSONObject.parseObject(JSONObject.toJSONString(user)).toJavaObject(HashMap.class)).collect(Collectors.toList());
        List<Map<String, String>> listMap = new ArrayList<Map<String, String>>();
        initUserInfoData().forEach(user -> {
            HashMap<String, String> hashMap = new HashMap<String, String>() {{
                put("id", user.getId() + "");
                put("name", user.getName());
                put("age", user.getAge() + "");
                put("birthday", user.getBirthday() + "");
                put("city", user.getCity());
                put("province", user.getProvince());
                put("score", user.getScore() + "");
            }};
            listMap.add(hashMap);
        });
        map.put("maplist", listMap);
        map.put("date", DateUtil.now());
        // 导出数据
        TemplateExportParams exportParams = new TemplateExportParams(new ClassPathResource("/templates/list.xlsx").getPath(),true);
        EasyPoiExcelUtil.exportTemplateExcel(exportParams, map, "list", response);
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
