package local.kongyu.log.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 错误日志记录类
 *
 * @author 孔余
 * @since 2023-03-15 15:38
 */
@Data
public class LogExceptionEntity implements Serializable {

    private static final long serialVersionUID = 7571752296816657892L;
    /**
     * null
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 名称
     */
    private String name;

    /**
     * 请求参数
     */
    private String reqParam;

    /**
     * 返回参数
     */
    private String resParam;

    /**
     * 异常信息
     */
    private String message;

    /**
     * 方法
     */
    private String method;

    /**
     * 请求URI
     */
    private String uri;

    /**
     * ip
     */
    private String ip;

    /**
     * 操作版本号
     */
    private String version;

    /**
     * 创建时间
     */
    private Date createTime;
}
