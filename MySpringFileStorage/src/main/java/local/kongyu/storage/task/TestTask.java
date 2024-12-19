package local.kongyu.storage.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.x.file.storage.core.FileStorageProperties;
import org.dromara.x.file.storage.core.FileStorageService;
import org.dromara.x.file.storage.core.FileStorageServiceBuilder;
import org.dromara.x.file.storage.core.platform.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TestTask {
    private final FileStorageService fileStorageService;//注入实列
    private CopyOnWriteArrayList<FileStorage> fileStorageList;


    @Scheduled(cron = "0 * * * * ?")
    public void checkFtpHealth() {
        this.initFileStorage();
        String platform = fileStorageList.get(0).getPlatform();
        int size = fileStorageService.listFiles().setPlatform(platform).listFiles().getFileList().size();
        log.info("访问FTP成功，目录下文件数量：{}", size);
        this.stopFileStorage();
    }

    public void initFileStorage() {
        fileStorageList = fileStorageService.getFileStorageList();
        FileStorageProperties.FtpConfig config = new FileStorageProperties.FtpConfig();
        config.setPlatform("vsftp");
        config.setHost("192.168.1.13");
        config.setPort(21);
        config.setUser("admin");
        config.setPassword("Admin@123");
        config.setDomain("ftp://192.168.1.13/");
        config.setBasePath("data/");
        config.setStoragePath("/");
        config.setIsActive(false);
        config.setConnectionTimeout(60000);
        config.setSoTimeout(60000);
        fileStorageList.addAll(FileStorageServiceBuilder.buildFtpFileStorage(Collections.singletonList(config),null));
    }

    public void stopFileStorage() {
        //删除
        FileStorage myStorage = fileStorageService.getFileStorage("vsftp");
        fileStorageList.remove(myStorage);
        myStorage.close();//释放资源
    }

}
