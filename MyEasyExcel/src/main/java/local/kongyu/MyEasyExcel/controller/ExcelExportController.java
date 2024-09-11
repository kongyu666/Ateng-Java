package local.kongyu.MyEasyExcel.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.fastjson2.JSONObject;
import local.kongyu.MyEasyExcel.entity.UserInfoEntity;
import local.kongyu.MyEasyExcel.init.InitData;
import local.kongyu.MyEasyExcel.utils.EasyExcelExportParam;
import local.kongyu.MyEasyExcel.utils.EasyExcelUtils;
import local.kongyu.MyEasyExcel.vo.ImageInfoVo;
import local.kongyu.MyEasyExcel.vo.UserInfoVo;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * EasyExcel导出Excel
 *
 * @author 孔余
 * @since 2024-02-05 17:34
 */
@RestController
@RequestMapping("/excel/export")
public class ExcelExportController {
    /**
     * 导出单个sheet Excel
     */
    @GetMapping("/simple")
    public void exportExcel(HttpServletResponse response) {
        List<List<String>> headList = Collections.singletonList(Arrays.asList("用户ID", "用户姓名", "用户年龄", "分数", "用户生日", "用户所在省份", "用户所在城市"));
        //rotateHead这里是为了旋转表头便于开发和理解
        List<List<String>> lists = EasyExcelUtils.rotateHead(headList);
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("用户信息").
                headList(lists).data(new InitData(1000).getList()).build();
        EasyExcelUtils.writeExcel(response, easyExcelExportParam);
    }

    /**
     * 导出CSV
     */
    @GetMapping("/simple/csv")
    public void exportCsv(HttpServletResponse response) {
        List<List<String>> headList = Collections.singletonList(Arrays.asList("用户ID", "用户姓名", "用户年龄", "分数", "用户生日", "用户所在省份", "用户所在城市"));
        //rotateHead这里是为了旋转表头便于开发和理解
        List<List<String>> lists = EasyExcelUtils.rotateHead(headList);
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("用户信息").
                headList(lists).data(new InitData(1000).getList()).build();
        EasyExcelUtils.writeCsv(response, easyExcelExportParam);
    }

    /**
     * 导出单个sheet Excel到本地
     * http://localhost:31324/excel/export/simple/local?pathName=/data/export/用户信息.xlsx
     */
    @GetMapping("/simple/local")
    public JSONObject exportExcel(String pathName) {
        List<List<String>> headList = Collections.singletonList(Arrays.asList("用户ID", "用户姓名", "用户年龄", "分数", "用户生日", "用户所在省份", "用户所在城市"));
        //rotateHead这里是为了旋转表头便于开发和理解
        List<List<String>> lists = EasyExcelUtils.rotateHead(headList);
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("用户信息").
                headList(lists).data(new InitData(1000).getList()).build();
        EasyExcelUtils.writeExcel(pathName, easyExcelExportParam);
        return JSONObject.of("msg", "导入成功！", "file", pathName);
    }

    /**
     * 根据模板填充列表数据 导出Excel
     */
    @GetMapping("/list/fill")
    public void exportExcelListFill(HttpServletResponse response) {
        // 列表中对象的属性需要和file中的参数对应
        List<UserInfoEntity> list = new InitData(1000).getList();
        //JSONArray array = JSONArray.parse(JSONArray.toJSONString(list)); // 属性对应即可
        String file = "doc/用户信息模板.xlsx";
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder()
                .excelName("用户信息")
                .sheetName("用户信息")
                .data(list)
                .build();
        EasyExcelUtils.writeExcelListFill(response, easyExcelExportParam, file);
    }

    /**
     * 根据模板填充列表数据 导出Excel(包含图片)
     */
    @GetMapping("/list/fill-image")
    public void exportExcelListFillImage(HttpServletResponse response) throws MalformedURLException {
        // 列表中对象的属性需要和file中的参数对应
        ArrayList<ImageInfoVo> list = new ArrayList<ImageInfoVo>() {{
            add(new ImageInfoVo(1, "图片1", new URL("http://192.168.1.12:9000/data/image/logo1.jpg")));
            add(new ImageInfoVo(2, "图片2", new URL("http://192.168.1.12:9000/data/image/logo2.jpg")));
            add(new ImageInfoVo(3, "图片3", new URL("http://192.168.1.12:9000/data/image/logo3.jpg")));
        }};
        String file = "doc/图片信息模板.xlsx";
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder()
                .excelName("图片信息")
                .sheetName("图片信息")
                .data(list)
                .build();
        EasyExcelUtils.writeExcelListFill(response, easyExcelExportParam, file);
    }

    /**
     * 根据模板填充列表数据 导出Excel(自定义多Sheet填充)
     */
    @GetMapping("/list/fill-custom")
    public void exportExcelListFillcustom(HttpServletResponse response) throws IOException {
        ExcelWriter excelWriter = null;
        try {
            OutputStream outputStream = response.getOutputStream();
            response.setHeader("Content-disposition", "attachment;filename=" + "11111.xlsx");
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");//设置类型
            excelWriter = EasyExcel.write(outputStream).withTemplate(new ClassPathResource("doc/自定义填充模板.xlsx").getInputStream()).build();
            // 第一个sheet
            WriteSheet writeSheet = EasyExcel.writerSheet("用户信息").build();

            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.FALSE).build();
            excelWriter.fill(new InitData(10).getList(), fillConfig, writeSheet);
            // 第二个sheet
            WriteSheet writeSheet2 = EasyExcel.writerSheet("其他信息").build();
            FillConfig fillConfig2 = FillConfig.builder().autoStyle(false).forceNewRow(Boolean.TRUE).build();
            // 图片绘制位置
            Workbook workbook = excelWriter.writeContext().writeWorkbookHolder().getWorkbook();
            CreationHelper creationHelper = workbook.getCreationHelper();
            ClientAnchor clientAnchor = creationHelper.createClientAnchor();
            clientAnchor.setCol1(0);
            clientAnchor.setCol2(2);
            clientAnchor.setRow1(5);
            clientAnchor.setRow2(10);
            Drawing<?> drawingPatriarch = workbook.getSheet("其他信息").createDrawingPatriarch();
            // 填充数据
            excelWriter.fill(new JSONObject(){{
                put("myName","阿腾");
                put("myDate", DateUtil.now());
                put("myDesc", "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
                //put("myImage", HttpUtil.downloadBytes("http://192.168.1.12:9000/data/image/logo1.jpg"));
            }}, fillConfig2, writeSheet2);
            // 插入图片
            byte[] bytes = HttpUtil.downloadBytes("http://192.168.1.12:9000/data/image/logo1.jpg");
            int picture = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            drawingPatriarch.createPicture(clientAnchor,picture);

            // 写入到文件
            excelWriter.finish();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            excelWriter.close();
        }

    }

    /**
     * 导出Excel包含图片
     */
    @GetMapping("/simple/image")
    public void exportExcelImage(HttpServletResponse response) throws MalformedURLException {
        ArrayList<ImageInfoVo> list = new ArrayList<ImageInfoVo>() {{
            add(new ImageInfoVo(1, "图片1", new URL("http://192.168.1.12:9000/data/image/logo1.jpg")));
            add(new ImageInfoVo(2, "图片2", new URL("http://192.168.1.12:9000/data/image/logo2.jpg")));
            add(new ImageInfoVo(3, "图片3", new URL("http://192.168.1.12:9000/data/image/logo3.jpg")));
        }};
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder()
                .excelName("用户信息")
                .openNote(true)
                .data(list)
                .build();
        EasyExcelUtils.writeExcel(response, easyExcelExportParam);
    }

    /**
     * 导出单个Excel模板
     * 数据置为空
     */
    @GetMapping("/simple/template")
    public void exportExcelTemplate(HttpServletResponse response) {
        List<List<String>> headList = Collections.singletonList(Arrays.asList("用户ID", "用户姓名", "用户年龄", "分数", "用户生日", "用户所在省份", "用户所在城市"));
        //rotateHead这里是为了旋转表头便于开发和理解
        List<List<String>> lists = EasyExcelUtils.rotateHead(headList);
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("用户信息").
                headList(lists).data(new ArrayList<UserInfoEntity>() {
                }).build();
        EasyExcelUtils.writeExcel(response, easyExcelExportParam);
    }

    /**
     * 注解：导出单个sheet Excel
     */
    @GetMapping("/note")
    public void exportExcelNote(HttpServletResponse response) {
        List<UserInfoEntity> userList = new InitData(1000).getList();
        List<UserInfoVo> userVoList = userList.stream().map(user -> {
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtils.copyProperties(user, userInfoVo);
            return userInfoVo;
        }).collect(Collectors.toList());
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("用户信息").
                openNote(true).data(userVoList).build();
        EasyExcelUtils.writeExcel(response, easyExcelExportParam);
    }

    /**
     * 注解：导出单个sheet Excel模板
     */
    @GetMapping("/note/template")
    public void exportExcelNoteTemplate(HttpServletResponse response) {
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("用户信息").
                //这个单括号很重要,因为注解版导出模板需要取出空集合里面泛型的Class
                        openNote(true).data(new ArrayList<UserInfoVo>() {
                }).build();
        EasyExcelUtils.writeExcel(response, easyExcelExportParam);
    }

    /**
     * 导出多个sheet空间的Excel
     */
    @GetMapping("/great/sheet")
    public void exportExcelGreatSheet(HttpServletResponse response) {
        List<List<String>> headList = Collections.singletonList(Arrays.asList("用户ID", "用户姓名", "用户年龄", "分数", "用户生日", "用户所在省份", "用户所在城市"));
        //rotateHead这里是为了旋转表头便于开发和理解
        List<List<String>> lists1 = EasyExcelUtils.rotateHead(headList);
        List<List<String>> lists2 = EasyExcelUtils.rotateHead(headList);
        List<List<String>> lists3 = EasyExcelUtils.rotateHead(headList);
        Set<String> greatSheet = new TreeSet<>(Arrays.asList("Sheet", "Sheet1", "Sheet2"));
        List<UserInfoEntity> dataList1 = new InitData(1000).getList();
        List<UserInfoEntity> dataList2 = new InitData(1000).getList();
        List<UserInfoEntity> dataList3 = new InitData(1000).getList();
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("用户信息").
                sheetNameSetGreatSheet(greatSheet).openNote(false).
                headListGreatSheet(Arrays.asList(lists1, lists2, lists3)).
                dataList(Arrays.asList(dataList1, dataList2, dataList3)).build();
        EasyExcelUtils.writeExcelGreatSheet(response, easyExcelExportParam);
    }

    /**
     * 导出多个sheet空间的Excel模板
     */
    @GetMapping("/great/sheet/template")
    public void exportExcelGreatSheetTemplate(HttpServletResponse response) {
        List<List<String>> headList = Collections.singletonList(Arrays.asList("用户ID", "用户姓名", "用户年龄", "分数", "用户生日", "用户所在省份", "用户所在城市"));
        //rotateHead这里是为了旋转表头便于开发和理解
        List<List<String>> lists1 = EasyExcelUtils.rotateHead(headList);
        List<List<String>> lists2 = EasyExcelUtils.rotateHead(headList);
        List<List<String>> lists3 = EasyExcelUtils.rotateHead(headList);
        Set<String> greatSheet = new TreeSet<>(Arrays.asList("Sheet", "Sheet1", "Sheet2"));
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("用户信息").
                sheetNameSetGreatSheet(greatSheet).
                headListGreatSheet(Arrays.asList(lists1, lists2, lists3)).
                dataList(Arrays.asList(new ArrayList<UserInfoEntity>() {
                }, new ArrayList<UserInfoEntity>() {
                }, new ArrayList<UserInfoEntity>() {
                })).build();
        EasyExcelUtils.writeExcelGreatSheet(response, easyExcelExportParam);
    }

    /**
     * 注解：导出多个sheet空间的Excel
     */
    @GetMapping("/great/sheet/template/note")
    public void exportExcelGreatSheetTemplateNote(HttpServletResponse response) {
        List<UserInfoEntity> userList1 = new InitData(1000).getList();
        List<UserInfoVo> userVoList1 = userList1.stream().map(user -> {
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtils.copyProperties(user, userInfoVo);
            return userInfoVo;
        }).collect(Collectors.toList());
        List<UserInfoEntity> userList2 = new InitData(1000).getList();
        List<UserInfoVo> userVoList2 = userList2.stream().map(user -> {
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtils.copyProperties(user, userInfoVo);
            return userInfoVo;
        }).collect(Collectors.toList());
        List<UserInfoEntity> userList3 = new InitData(1000).getList();
        List<UserInfoVo> userVoList3 = userList3.stream().map(user -> {
            UserInfoVo userInfoVo = new UserInfoVo();
            BeanUtils.copyProperties(user, userInfoVo);
            return userInfoVo;
        }).collect(Collectors.toList());

        Set<String> strings3 = new TreeSet<>(Arrays.asList("Sheet", "Sheet1", "Sheet2"));
        EasyExcelExportParam easyExcelExportParam = EasyExcelExportParam.builder().excelName("123").
                sheetNameSetGreatSheet(strings3).openNote(true).
                dataList(Arrays.asList(userVoList1, userVoList2, userVoList3)).build();
        EasyExcelUtils.writeExcelGreatSheet(response, easyExcelExportParam);
    }

}
