package local.kongyu.gracefulResponse.exception;

import com.feiniaojin.gracefulresponse.api.ExceptionAliasFor;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 12:20
 */
@ExceptionAliasFor(code = "-1", msg = "数组越界异常", aliasFor = {IndexOutOfBoundsException.class})
public class MyIndexOutOfBoundsException extends RuntimeException{
}
