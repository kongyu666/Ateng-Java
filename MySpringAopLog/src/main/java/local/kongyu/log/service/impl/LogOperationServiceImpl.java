package local.kongyu.log.service.impl;

import local.kongyu.log.entity.LogOperationEntity;
import local.kongyu.log.service.LogOperationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 正常日志记录类的service的实现
 *
 * @author 孔余
 * @since 2023-03-15 14:10
 */
@Service
@Slf4j
public class LogOperationServiceImpl implements LogOperationService {
    @Override
    public String save(LogOperationEntity operation) {
        // 这里就不做具体实现了
        log.info("{}", operation);
        return "ok";
    }
}
