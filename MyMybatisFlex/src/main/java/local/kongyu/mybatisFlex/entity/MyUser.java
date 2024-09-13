package local.kongyu.mybatisFlex.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 *  实体类。
 *
 * @author 孔余
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "my_user")
public class MyUser implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号，可选

    @Id(keyType = KeyType.Auto)
    private Long id;
    private String name;
    private Integer age;
    private Double score;
    private Date birthday;
    private String province;
    private String city;
    private Date createTime;
}
