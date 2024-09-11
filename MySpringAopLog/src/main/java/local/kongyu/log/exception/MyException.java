package local.kongyu.log.exception;

/**
 * 自定义异常
 *
 * @author 孔余
 * @since 2023-03-20 16:24:32
 */
public class MyException extends RuntimeException {
    private static final long serialVersionUID = 17623062936L;
    private Integer code;
    private String msg;

    public Integer getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }

    public MyException(Integer code, String msg) {
        super(Result.error(code, msg).toString());
        this.code = code;
        this.msg = msg;
    }
}
