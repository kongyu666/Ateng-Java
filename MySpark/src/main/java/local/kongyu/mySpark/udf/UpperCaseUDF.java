package local.kongyu.mySpark.udf;

import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;

import static org.apache.spark.sql.functions.udf;

/**
 * 小写转大写
 * 一元UDF
 *
 * @author 孔余
 * @since 2024-01-31 21:09
 */
public class UpperCaseUDF implements UDF1<String, String> {
    @Override
    public String call(String input) throws Exception {
        if (input == null) {
            return null;
        }
        return input.toUpperCase();
    }

    // 创建 UserDefinedFunction
    public static UserDefinedFunction upperCaseUDF() {
        return udf(new UpperCaseUDF(), DataTypes.StringType);
    }
}
