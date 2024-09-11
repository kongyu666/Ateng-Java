package local.kongyu.gracefulResponse.exception;

import com.feiniaojin.gracefulresponse.api.ExceptionMapper;

/**
 * Graceful Response引入@ExceptionMapper注解，通过该注解将异常和错误码关联起来，
 * 这样Service方法就不需要再维护Response的响应码了，
 * 直接抛出业务异常，由Graceful Response进行异常和响应码的关联。
 *
 *  NotFoundException的定义，使用@ExceptionMapper注解修饰
 *  code:代表接口的异常码
 *  msg:代表接口的异常提示
 * @author 孔余
 * @since 2024-01-17 10:30
 */
@ExceptionMapper(code = "-1", msg = "资源不存在")
public class NotFoundException extends RuntimeException{
}
