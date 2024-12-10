package local.kongyu.hutool;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

public class FtpUtilTests {

    @Test
    public void testFtpUtil() throws IOException {
        // 创建FTP客户端对象
        Ftp ftp = new Ftp("183.64.162.93", 21, "admin", "Lingo@12345");
//切换为被动模式
        ftp.setMode(FtpMode.Passive);

        try {
            // 要上传的字符串内容
            String content = "[{\"id\":56873,\"name\":\"李尚银\",\"code\":\"H-99078\",\"risk\":\"低风险\",\"town\":\"石壕镇\",\"status\":\"否\",\"createTime\":\"2024-11-12 18:00:37.073\"}]";

            // 指定上传的远程路径和文件名
            String remotePath = "/data";  // 远程目录
            String fileName = "rrs-risk-1234567890.txt";  // 上传后的文件名

            // 将字符串内容转换为输入流
            InputStream inputStream = IoUtil.toStream(content, CharsetUtil.CHARSET_UTF_8);

            // 上传文件
            boolean result = ftp.upload(remotePath, fileName, inputStream);

            // 检查上传结果
            if (result) {
                System.out.println("文件上传成功！");
            } else {
                System.out.println("文件上传失败！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭FTP连接
            ftp.close();
        }
    }
}
