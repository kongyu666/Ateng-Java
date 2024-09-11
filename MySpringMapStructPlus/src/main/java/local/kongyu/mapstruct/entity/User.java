package local.kongyu.mapstruct.entity;

import io.github.linpeilie.annotations.AutoMapper;
import io.github.linpeilie.annotations.AutoMapping;
import local.kongyu.mapstruct.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-05-28 11:10
 */
@AutoMapper(target = UserDTO.class)
@Data
@AllArgsConstructor
public class User {
    @AutoMapping(target = "name")
    private String fullName;
    @AutoMapping(target = "age")
    private int years;
    private boolean young;
    private Timestamp timestamp;
    @AutoMapping(target = "dateTimeStr", dateFormat = "yyyy-MM-dd")
    private Date dateTime;
}
