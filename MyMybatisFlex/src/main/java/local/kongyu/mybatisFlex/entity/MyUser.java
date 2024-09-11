package local.kongyu.mybatisFlex.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Date;

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

    /**
     * user id
     */
    private String userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date date;

    private String city;

    private Integer age;

    private Integer sex;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastVisitDate;

    private Long cost;

    private Integer maxDwellTime;

    private Integer minDwellTime;

}
