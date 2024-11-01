package local.kongyu.storage;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.RequiredArgsConstructor;
import org.dromara.x.file.storage.core.FileInfo;
import org.dromara.x.file.storage.core.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class FileStorageApplicationTests {
    private final FileStorageService fileStorageService;


    @Test
    void contextLoads() {
        // 时间
        DateTime dateTime = DateUtil.date();
        String dateTimeStr = DateUtil.format(dateTime, "yyyy/MM/dd/HH/mm/");
        String fileUrl = "http://dev.minio.lingo.local/test/mysql-connector-j-8.0.33.jar";
        FileInfo uploadFile = fileStorageService.of(fileUrl).setPath(dateTimeStr).setOriginalFilename(fileUrl).upload();
        String uploadFileUrl = uploadFile.getUrl();
        System.out.println(uploadFileUrl);
    }

}
