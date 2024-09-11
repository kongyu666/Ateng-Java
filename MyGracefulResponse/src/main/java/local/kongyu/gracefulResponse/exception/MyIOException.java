package local.kongyu.gracefulResponse.exception;

import com.feiniaojin.gracefulresponse.api.ExceptionAliasFor;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 类的模板注释
 *
 * @author 孔余
 * @since 2024-01-17 12:20
 */
@ExceptionAliasFor(code = "-1", msg = "文件操作异常", aliasFor = {IOException.class, FileNotFoundException.class})
public class MyIOException extends RuntimeException{
}
