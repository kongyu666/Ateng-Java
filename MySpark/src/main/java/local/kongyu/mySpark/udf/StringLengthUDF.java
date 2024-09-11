package local.kongyu.mySpark.udf;

import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;

import static org.apache.spark.sql.functions.udf;

/**
 * 数据长度
 * 一元UDF
 *
 * @author 孔余
 * @since 2024-2-2 16:58
 */
public class StringLengthUDF implements UDF1<String, Integer> {
    @Override
    public Integer call(String input) throws Exception {
        if (input == null) {
            return null;
        }
        return input.length();
    }

    // 创建 UserDefinedFunction
    public static UserDefinedFunction stringLengthUDF() {
        return udf(new StringLengthUDF(), DataTypes.IntegerType);
    }
}
