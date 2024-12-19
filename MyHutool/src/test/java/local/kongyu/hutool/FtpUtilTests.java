package local.kongyu.hutool;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpConfig;
import cn.hutool.extra.ftp.FtpMode;
import org.junit.jupiter.api.Test;

import java.io.File;
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

    @Test
    public void testFtpUtil2() {
        // FTP服务器的配置信息
        String ftpHost = "ftp.example.com";
        String username = "yourUsername";
        String password = "yourPassword";
        int port = 21; // 默认FTP端口
        String remoteDir = "/remote/dir"; // 远程目录
        String localFilePath = "path/to/local/file.txt"; // 本地文件路径
        String remoteFilePath = remoteDir + "/file.txt"; // 远程文件路径

        // 设置连接超时时间和读取超时时间（单位：毫秒）
        int connectTimeout = 5000; // 连接超时 5秒
        int readTimeout = 5000; // 读取超时 5秒

        // 创建FtpConfig对象，配置FTP相关信息
        FtpConfig config = FtpConfig.create()
                .setHost(ftpHost)
                .setPort(port)
                .setUser(username)
                .setPassword(password)
                .setConnectionTimeout(connectTimeout)
                .setSoTimeout(readTimeout);

        // 创建 Ftp 客户端
        try (Ftp ftp = new Ftp(config, FtpMode.Passive)) {

            // 切换到远程目录
            ftp.cd(remoteDir);


            // 上传文件
            File localFile = FileUtil.file(localFilePath);
            boolean uploadSuccess = ftp.upload(remoteFilePath, localFile);
            if (uploadSuccess) {
                System.out.println("文件上传成功！");
            } else {
                System.out.println("文件上传失败！");
            }
        } catch (IOException e) {
            System.out.println("FTP操作失败: " + e.getMessage());
        }
    }
}
