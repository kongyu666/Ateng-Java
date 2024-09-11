package local.kongyu.mySpark.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-29 17:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private Long id;
    private String uuid;
    private String name;
    private String region;
    private Integer age;
    private String email;
    private Integer salary;
    private Double score;
    private Timestamp dateTime;

    // 添加构造方法或静态方法用于数据转换
    public static User fromCsvString(String csvString) {
        String[] fields = csvString.split(",");
        if (fields.length != 9) {
            throw new IllegalArgumentException("Invalid CSV string format");
        }

        User user = new User();
        user.setId(Long.parseLong(fields[0]));
        user.setUuid(fields[1]);
        user.setName(fields[2]);
        user.setRegion(fields[3]);
        user.setAge(Integer.parseInt(fields[4]));
        user.setEmail(fields[5]);
        user.setSalary(Integer.parseInt(fields[6]));
        user.setScore(Double.parseDouble(fields[7]));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            user.setDateTime(new Timestamp(dateFormat.parse(fields[8]).getTime()));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format");
        }

        return user;
    }
}
