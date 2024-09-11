package local.kongyu.gracefulResponse.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

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
public class ClassInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    @NotNull(message = "用户名不能为空")
    private String name;
    @NotEmpty(message = "列表不能为空")
    @Valid
    private List<UserInfo> userInfoList;
}
