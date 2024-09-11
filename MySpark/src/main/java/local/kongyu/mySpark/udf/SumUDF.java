package local.kongyu.mySpark.udf;

import org.apache.spark.sql.api.java.UDF2;
import org.apache.spark.sql.expressions.UserDefinedFunction;
import org.apache.spark.sql.types.DataTypes;

import static org.apache.spark.sql.functions.udf;

/**
 * 数据长度
 * 二元UDF
 *
 * @author 孔余
 * @since 2024-2-2 16:58
 */
public class SumUDF implements UDF2<Integer, Integer, Integer> {
    @Override
    public Integer call(Integer a, Integer b) throws Exception {
        return a + b;
    }

    // 创建 UserDefinedFunction
    public static UserDefinedFunction sumUDF() {
        return udf(new SumUDF(), DataTypes.IntegerType);
    }
}
