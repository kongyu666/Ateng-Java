package local.kongyu.MyEasyPoi.utils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Excel导入导出工具类
 *
 * @author 孔余
 * @since 2024-02-01 22:33:14
 */
@Slf4j
public class EasyPoiExcelUtil {
    /**
     * 允许导出的最大条数
     */
    private static final Integer EXPORT_EXCEL_MAX_NUM = 100000;

    /**
     * excel 导出
     *
     * @param list      数据列表
     * @param title     表格内数据标题
     * @param sheetName sheet名称
     * @param pojoClass pojo类型
     * @param fileName  导出时的excel名称
     * @param response  HttpServletResponse
     */
    public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) {
        //给文件名拼接上时间戳
        long currentTimeMillis = System.currentTimeMillis();
        fileName = fileName + currentTimeMillis;
        //判断导出数据是否为空
        if (list == null) {
            list = new ArrayList<>();
        }
        //判断导出数据数量是否超过限定值
        if (list.size() > EXPORT_EXCEL_MAX_NUM) {
            title = "导出数据行数超过:" + EXPORT_EXCEL_MAX_NUM + "条,无法导出、请添加导出条件!";
            list = new ArrayList<>();
        }
        try {
            //获取导出参数
            ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
            //设置导出样式
            exportParams.setStyle(EasyPoiExcelStyleUtil.class);
            //设置行高
            exportParams.setHeight((short) 6);
            //把数据添加到excel表格中
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, pojoClass, list);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8").replaceAll("\\+", "%20"));
            workbook.setForceFormulaRecalculation(true); //强制开启excel公式计算
            workbook.write(response.getOutputStream());
            log.info("{}: 导出Excel文件【{}.xlsx】成功", pojoClass, fileName);
        } catch (IOException e) {
            log.error("{}: 导出Excel文件【{}.xlsx】失败", pojoClass, fileName);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            throw new RuntimeException();
        }
    }

    /**
     * excel 导出
     *
     * @param headMap   表头映射
     * @param dataList  数据列表，与表头对应
     * @param title     标题名称
     * @param sheetName sheet名称
     * @param fileName  导出时的excel名称
     * @param response  HttpServletResponse
     */
    public static void exportExcel(Map<Object, String> headMap, List<Map<String, Object>> dataList, String title, String sheetName, String fileName, HttpServletResponse response) {
        //给文件名拼接上时间戳
        long currentTimeMillis = System.currentTimeMillis();
        fileName = fileName + currentTimeMillis;
        //判断导出数据是否为空
        if (dataList == null) {
            dataList = new ArrayList<>();
        }
        //判断导出数据数量是否超过限定值
        if (dataList.size() > 20 * EXPORT_EXCEL_MAX_NUM) {
            title = "导出数据行数超过:" + EXPORT_EXCEL_MAX_NUM + "条,无法导出、请添加导出条件!";
            dataList = new ArrayList<>();
        }
        try {
            //获取导出参数
            ExportParams exportParams = new ExportParams(title, sheetName, ExcelType.XSSF);
            //设置导出样式
            exportParams.setStyle(EasyPoiExcelStyleUtil.class);
            //设置行高
            exportParams.setHeight((short) 6);
            // 自定义表头
            List<ExcelExportEntity> columnList = new ArrayList<>();
            for (Object headKey : headMap.keySet()) {
                columnList.add(new ExcelExportEntity(headMap.get(headKey), headKey));
            }
            //把数据添加到excel表格中
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, columnList, dataList);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8").replaceAll("\\+", "%20"));
            workbook.setForceFormulaRecalculation(true); //强制开启excel公式计算
            workbook.write(response.getOutputStream());
            log.info("导出Excel文件【{}.xlsx】成功", fileName);
        } catch (IOException e) {
            log.error("导出Excel文件【{}.xlsx】失败", fileName);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            throw new RuntimeException();
        }
    }

    /**
     * excel 模板导出
     * @param exportParams
     * @param dataMap
     * @param fileName
     * @param response
     */
    public static void exportTemplateExcel(TemplateExportParams exportParams, Map<String, Object> dataMap, String fileName, HttpServletResponse response) {
        //给文件名拼接上时间戳
        long currentTimeMillis = System.currentTimeMillis();
        fileName = fileName + currentTimeMillis;
        try {
            //把数据添加到excel表格中
            Workbook workbook = ExcelExportUtil.exportExcel(exportParams, dataMap);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", "UTF-8").replaceAll("\\+", "%20"));
            workbook.setForceFormulaRecalculation(true); //强制开启excel公式计算
            workbook.write(response.getOutputStream());
            log.info("导出Excel文件【{}.xlsx】成功", fileName);
        } catch (IOException e) {
            log.error("导出Excel文件【{}.xlsx】失败", fileName);
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            throw new RuntimeException();
        }
    }

    /**
     * excel 导入
     *
     * @param file      excel文件
     * @param pojoClass pojo类型
     * @param <T>
     * @return
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> pojoClass, Integer titleRows, Integer headerRows) {
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        params.setNeedVerify(true);
        try {
            ExcelImportResult<Object> objectExcelImportResult = ExcelImportUtil.importExcelMore(file.getInputStream(), pojoClass, params);
            boolean verifyFail = objectExcelImportResult.isVerifyFail();
            if (verifyFail) {
                return new ArrayList<>();
            }
            return (List<T>) objectExcelImportResult.getList();
        } catch (Exception e) {
            log.error("{}: 导入Excel文件【{}】失败", pojoClass, file.getOriginalFilename());
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

}

