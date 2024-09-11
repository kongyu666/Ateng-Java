package local.kongyu.log.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletResponse;

/**
 * 统一处理异常
 *
 * @author 孔余
 * @since 2023-03-20 16:23:02
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {
    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result exception(Exception e) {
        log.error("全局异常信息: {}", e.getMessage());
        e.printStackTrace();
        return Result.error(BizCodeEnume.MY_EXCEPTION.getCode(), BizCodeEnume.MY_EXCEPTION.getMsg());
    }

    /**
     * 自定义异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MyException.class)
    @ResponseBody
    public Result myException(MyException e) {
        log.error("自定义异常信息: {}", e.getMessage());
        e.printStackTrace();
        return Result.error(e.getCode(), e.getMsg());
    }

    /**
     * 错误接口返回异常
     *
     * @param response
     * @param e
     * @return
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    public Result exception(HttpServletResponse response, Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        // 设置返回的状态码和头信息
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Company", "Lingo");
        response.setHeader("Author", "KongYu");
        return Result.error(response.getStatus(), HttpStatus.NOT_FOUND.getReasonPhrase()).setData("没有这个接口");
    }
}

