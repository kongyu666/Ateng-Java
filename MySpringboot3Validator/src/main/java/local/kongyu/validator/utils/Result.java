package local.kongyu.validator.utils;


import com.alibaba.fastjson2.JSON;
import local.kongyu.validator.constant.AppCodeEnum;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一结果返回处理，支持链式调用
 *
 * @param <T> 返回的数据类型
 * @since 2024-05-30 15:16:07
 */
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String code; // 状态码
    private String msg; // 提示信息
    private T data; // 返回的数据

    /**
     * 默认构造方法，设置初始的状态码和提示信息
     */
    public Result() {
        this.code = AppCodeEnum.SUCCESS.getCode();
        this.msg = AppCodeEnum.SUCCESS.getDescription();
    }

    /**
     * 静态方法，返回一个错误结果
     */
    public static Result error() {
        return error(AppCodeEnum.ERROR.getCode(), AppCodeEnum.ERROR.getDescription());
    }

    /**
     * 静态方法，返回一个带自定义状态码和提示信息的错误结果
     */
    public static Result error(String code, String msg) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /**
     * 静态方法，返回一个带自定义状态码和自定义提示信息的错误结果
     */
    public static Result error(String msg) {
        return error(AppCodeEnum.ERROR.getCode(), msg);
    }

    /**
     * 静态方法，返回一个默认的成功结果
     */
    public static Result success() {
        return new Result();
    }

    /**
     * 静态方法，返回一个带自定义状态码和自定义提示信息的成功结果
     */
    public static Result success(String msg) {
        return success(AppCodeEnum.SUCCESS.getCode(), msg);
    }

    /**
     * 静态方法，返回一个带自定义状态码和提示信息的成功结果
     */
    public static Result success(String code, String msg) {
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        return r;
    }

    /**
     * 静态方法，返回一个包含指定数据的成功结果
     */
    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setData(data);
        return r;
    }

    /**
     * 获取返回的数据
     */
    public Object getData() {
        return data;
    }

    /**
     * 设置返回的数据
     */
    public Result<T> setData(T data) {
        this.data = data;
        return this;
    }

    /**
     * 获取data里面的数据，并且设置泛型可以直接转换出对应的类型
     */
    public <T> T getData(Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(this.getData()), clazz);
    }

    /**
     * 获取状态码
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置状态码
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取提示信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置提示信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }

    /**
     * 转换为JSON字符串
     */
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
