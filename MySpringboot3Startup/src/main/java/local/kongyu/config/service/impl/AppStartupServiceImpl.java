package local.kongyu.config.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.annotation.PostConstruct;
import local.kongyu.config.service.AppStartupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AppStartupServiceImpl implements AppStartupService {
    /**
     * @PostConstruct：在Bean初始化后执行的方法。
     */
    @Override
    @PostConstruct
    public void startup1() {
        log.info("由{}启动...", "@PostConstruct");
    }

    /**
     * @EventListener(ApplicationReadyEvent.class)：在应用完全启动后执行的方法。
     */
    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void startup2() {
        log.info("由{}启动...", "@EventListener(ApplicationReadyEvent.class)");
    }

    /**
     * 获取Bean
     */
    @Override
    @EventListener(ApplicationReadyEvent.class)
    public void startup3() {
        String myBean = SpringUtil.getApplicationContext().getBean("myBean", String.class);
        log.info("myBean={}", myBean);
    }
}
