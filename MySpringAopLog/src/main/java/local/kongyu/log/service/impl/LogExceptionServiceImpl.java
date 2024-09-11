package local.kongyu.log.service.impl;

import local.kongyu.log.entity.LogExceptionEntity;
import local.kongyu.log.service.LogExceptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 错误日志记录类的service的实现
 *
 * @author 孔余
 * @since 2023-03-15 14:10
 */
@Service
@Slf4j
public class LogExceptionServiceImpl implements LogExceptionService {
    @Override
    public String save(LogExceptionEntity exception) {
        // 这里就不做具体实现了
        log.error("{}", log);
        return "ok";
    }
}
