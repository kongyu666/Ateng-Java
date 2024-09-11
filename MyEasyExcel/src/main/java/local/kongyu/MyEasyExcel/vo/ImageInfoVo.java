package local.kongyu.MyEasyExcel.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.converters.url.UrlImageConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URL;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-02-05 22:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ContentRowHeight(75)
@HeadRowHeight(25)
@ColumnWidth(25)
public class ImageInfoVo {
    @ExcelProperty(value = "序号")
    private Integer id;
    @ExcelProperty(value = "名字")
    private String name;
    @ExcelProperty(value = "图片", converter = UrlImageConverter.class)
    @ColumnWidth(25)
    private URL url;
}
