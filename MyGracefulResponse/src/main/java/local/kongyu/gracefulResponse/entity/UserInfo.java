package local.kongyu.gracefulResponse.entity;

import com.feiniaojin.gracefulresponse.api.ValidationStatusCode;
import local.kongyu.gracefulResponse.config.MyValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 10:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "用户名不能为空")
    @Length(min = 1, max = 3, message = "用户名太长")
    @ValidationStatusCode(code = "-3")
    private String name;
    @Min(value = 0, message = "年龄太小")
    @Max(value = 100, message = "年龄太大")
    private Integer age;

    @NotBlank(message = "用户名不能为空", groups = {MyValidationGroups.Create.class})
    private String username;
    @NotBlank(message = "密码不能为空", groups = {MyValidationGroups.Create.class, MyValidationGroups.Update.class})
    private String password;
}
