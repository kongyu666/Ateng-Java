package local.kongyu.log.exception;

/**
 * 系统异常枚举类
 */

public enum BizCodeEnume {
    /**
     *
     */
    SUCCESS(200, "请求成功"),
    MY_EXCEPTION(500, "服务器内部错误");
    private int code;
    private String msg;

    BizCodeEnume(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
