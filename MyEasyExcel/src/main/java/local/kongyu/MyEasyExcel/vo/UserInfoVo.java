package local.kongyu.MyEasyExcel.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

import java.util.Date;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-02-05 22:21
 */
@Data
@ContentRowHeight(20)
@HeadRowHeight(25)
@ColumnWidth(25)
public class UserInfoVo {
    @ExcelProperty(value = "用户ID", index = 0)
    private Long id;
    @ExcelProperty(value = "用户姓名", index = 1)
    private String name;
    @ExcelProperty(value = "用户年龄", index = 2)
    private Integer age;
    @ExcelProperty(value = "分数", index = 3)
    //@NumberFormat(value = "#.##%",roundingMode = RoundingMode.HALF_UP) //数字格式化
    private Double score;
    @ExcelProperty(value = "用户生日", index = 4)
    @DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
    @ColumnWidth(50)
    private Date birthday;
    @ExcelProperty(value = "用户所在省份", index = 5)
    private String province;
    @ExcelProperty(value = "用户所在城市", index = 6)
    private String city;
}
