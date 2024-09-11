package local.kongyu.MyEasyPoi.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息实体类
 * 用于表示系统中的用户信息。
 *
 * @author 孔余
 * @since 2024-01-10 15:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @Excel(name = "用户ID")
    private Long id;

    /**
     * 用户姓名
     */
    @Excel(name = "用户姓名")
    private String name;

    /**
     * 用户年龄
     * 注意：这里使用Integer类型，表示年龄是一个整数值。
     */
    @Excel(name = "用户年龄")
    private Integer age;

    /**
     * 分数
     */
    @Excel(name = "分数")
    private Double score;

    /**
     * 用户生日
     * 注意：这里使用Date类型，表示用户的生日。
     */
    @Excel(name = "用户生日", format = "yyyy-MM-dd HH:mm:ss")
    private Date birthday;

    /**
     * 用户所在省份
     */
    @Excel(name = "用户所在省份")
    private String province;

    /**
     * 用户所在城市
     */
    @Excel(name = "用户所在城市")
    private String city;
}
