package local.kongyu.gracefulResponse.exception;

import com.feiniaojin.gracefulresponse.api.ExceptionAliasFor;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 12:20
 */
@ExceptionAliasFor(code = "-1", msg = "数据计算异常", aliasFor = {ArithmeticException.class})
public class MyArithmeticException extends RuntimeException{
}
