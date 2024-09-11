package local.kongyu.mybatisFlexPostGis.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import local.kongyu.mybatisFlexPostGis.handler.GeometryTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.postgis.jdbc.geometry.Geometry;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2023-07-27 13:46
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("my_location")
public class MyLocationEntity implements Serializable {
    private static final long serialVersionUID = 1L; // 序列化版本号，可选

    @Id(keyType = KeyType.Auto)
    private Long id;
    private Long userId;
    @Column(typeHandler = GeometryTypeHandler.class)
    private Geometry location;
    private Timestamp createTime;
}
