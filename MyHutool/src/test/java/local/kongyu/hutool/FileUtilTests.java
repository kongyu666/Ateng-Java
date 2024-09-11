package local.kongyu.hutool;

import cn.hutool.core.io.FileUtil;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * 文件工具类
 * https://www.hutool.cn/docs/#/core/IO/%E6%96%87%E4%BB%B6%E5%B7%A5%E5%85%B7%E7%B1%BB-FileUtil
 *
 * @author 孔余
 * @since 2024-01-17 21:34
 */
public class FileUtilTests {
    // 写入文件
    @Test
    void test01() {
        FileUtil.writeString("Hello World", "D:\\Temp\\202401\\my_user.sql", "UTF-8");
    }

    // 读取文件，并逐行处理
    @Test
    void test02() throws IOException {
        BufferedReader reader = FileUtil.getUtf8Reader("D:\\Temp\\202401\\my_user.sql");
        for (int i = 0; i < 5; i++) {
            String line = reader.readLine();
            System.out.println(line);
        }
    }

}
