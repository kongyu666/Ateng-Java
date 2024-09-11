package local.kongyu.gracefulResponse.exception;

import com.feiniaojin.gracefulresponse.api.ExceptionAliasFor;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 12:20
 */
@ExceptionAliasFor(code = "-1", msg = "数据类型不匹配异常", aliasFor = {MethodArgumentTypeMismatchException.class,NumberFormatException.class})
public class MyTypeMismatchException extends RuntimeException{
}
