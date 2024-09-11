package local.kongyu.MyEasyExcel.utils;

import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.Builder;
import lombok.Data;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @ClassName ExportExcelParam.java
 * @Author 李瑞珂 [18375669585@163.com]
 * @Version 1.0.0
 * @Description 导出Excel参数
 * @CreateTime 2022年04月28日 16:42:00
 */
@Data
@Builder
public class EasyExcelExportParam {

    public static final String DEFAULT_SHEET_NAME = "Sheet";

    /**
     * 要导出的Excel名称
     */
    private String excelName;
    /**
     * 要导出的文件格式(默认为xlsx)
     */
    @Builder.Default
    private ExcelTypeEnum fileType = ExcelTypeEnum.XLSX;
    /**
     * excel表头设置
     */
    private List<List<String>> headList;
    /**
     * 要导出的数据
     */
    private List<?> data;
    /**
     * 是否启用注解模式(默认不开启)
     */
    @Builder.Default
    private boolean openNote = false;
    /**
     * 图片文件
     */
    private File imageFile;
    /**
     * 设置sheet的名称(默认值sheet)
     */
    @Builder.Default
    private String sheetName = DEFAULT_SHEET_NAME;
    /**
     * 要导出多个sheet的时候需要传该List
     * 外面集合的长度要与sheetName对应
     */
    private List<List<?>> dataList;
    /**
     * excel表头设置(多个Sheet)
     */
    private List<List<List<String>>> headListGreatSheet;
    /**
     * 设置sheet的名称
     */
    private Set<String> sheetNameSetGreatSheet;
    /**
     * 根据文件导出 并设置导出的位置。
     */
    private WriteCellData<Void> writeCellDataFile;
}