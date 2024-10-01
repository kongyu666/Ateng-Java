package local.kongyu.config.service;

import local.kongyu.config.event.MyCustomEvent;

/**
 * 程序启动需要运行的代码
 */
public interface AppStartupService {
    void startup1();
    void startup2();
    void startup3();
    void startup4(MyCustomEvent myCustomEvent);
}
