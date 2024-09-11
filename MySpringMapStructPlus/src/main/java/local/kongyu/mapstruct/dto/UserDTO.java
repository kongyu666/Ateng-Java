package local.kongyu.mapstruct.dto;

import lombok.Data;

import java.sql.Timestamp;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-05-28 11:10
 */
@Data
public class UserDTO {
    private String name;
    private int age;
    private boolean young;
    private Timestamp timestamp;
    private String dateTimeStr;
}
