package local.kongyu.MyEasyExcel.controller;

import com.alibaba.fastjson2.JSONObject;
import local.kongyu.MyEasyExcel.entity.UserInfoEntity;
import local.kongyu.MyEasyExcel.utils.EasyExcelUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * EasyExcel导入Excel
 *
 * @author 孔余
 * @since 2024-02-05 17:34
 */
@RestController
@RequestMapping("/excel/import")
public class ExcelImportController {
    // 导入单个sheet Excel
    @PostMapping("/simple")
    public JSONObject importSimpleExcel(MultipartFile file, @RequestParam(required = false, defaultValue = "0") String id) throws IOException {
        List<UserInfoEntity> list = EasyExcelUtils.readExcelOneSheet(file, UserInfoEntity.class);
        System.out.println(list.get(list.size() - 1) + " =>" + list.size());
        return JSONObject.of("msg", "导入成功！" + id);
    }

    // 导入所有sheet Excel
    @PostMapping("/great/sheet")
    public JSONObject importExcelGreatSheet(MultipartFile file, @RequestParam(required = false, defaultValue = "0") String id) throws IOException {
        List<UserInfoEntity> list = EasyExcelUtils.readExcelAllSheet(file, UserInfoEntity.class);
        System.out.println(list.get(list.size() - 1) + " =>" + list.size());
        return JSONObject.of("msg", "导入成功！" + id);
    }
}
