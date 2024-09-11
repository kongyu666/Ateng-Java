package local.kongyu.log.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 正常日志记录类
 *
 * @author 孔余
 * @since 2023-03-15 15:38
 */
@Data
public class LogOperationEntity implements Serializable {

    private static final long serialVersionUID = 2899031301824275986L;
    /**
     * null
     */
    private String id;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 模块
     */
    private String modul;

    /**
     * 方法
     */
    private String method;

    /**
     * 类型
     */
    private String type;

    /**
     * 描述
     */
    private String describe;

    /**
     * 请求参数
     */
    private String reqParam;

    /**
     * 返回参数
     */
    private String resParam;

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
