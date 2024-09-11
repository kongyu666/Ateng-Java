package local.kongyu.gracefulResponse.exception;

import com.feiniaojin.gracefulresponse.api.ExceptionAliasFor;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 12:20
 */
@ExceptionAliasFor(code = "-1", msg = "不支持的操作异常", aliasFor = {UnsupportedOperationException.class})
public class MyUnsupportedOperationException extends RuntimeException{
}
