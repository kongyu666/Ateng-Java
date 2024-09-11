package local.kongyu.log.service;

import local.kongyu.log.entity.LogOperationEntity;

/**
 * 正常日志记录类的service
 *
 * @author 孔余
 * @since 2023-03-15 14:09:23
 */

public interface LogOperationService {
    String save(LogOperationEntity operation);
}
