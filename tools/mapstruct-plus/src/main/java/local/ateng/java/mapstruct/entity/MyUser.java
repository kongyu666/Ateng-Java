package local.ateng.java.mapstruct.entity;

import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import local.ateng.java.mapstruct.dto.MyUserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@AutoMapper(target = MyUserDto.class)
public class MyUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 邮箱
     */
    private String email;

    /**
     * IP地址
     */
    private String ipaddress;

    /**
     * 公司名称
     */
    private String company;

    /**
     * 分数
     */
    @AutoMapping(numberFormat = "$0.00")
    private BigDecimal score;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 所在省份
     */
    private String province;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 地址
     */
    private String address;

    /**
     * 经纬度(lat,lng)
     */
    private String location;

    /**
     * 一段文字
     */
    private String paragraph;

    /**
     * 记录创建时间，默认当前时间
     */
    @AutoMapping(target = "dateTime", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

}
