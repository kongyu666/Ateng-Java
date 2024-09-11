package local.kongyu.mybatisFlexPostGis.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2023-07-27 13:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("my_user")
public class MyUserEntity implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号，可选

    @Id(keyType = KeyType.Auto)
    private Long id;
    private String name;
    private Integer age;
    private BigDecimal score;
    private Date birthday;
    private String province;
    private String city;
    private Date createTime;
    private Date updateTime;
}
