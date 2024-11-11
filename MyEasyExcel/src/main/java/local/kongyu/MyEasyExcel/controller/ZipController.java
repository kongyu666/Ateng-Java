package local.kongyu.MyEasyExcel.controller;

import local.kongyu.MyEasyExcel.utils.WordUtil;
import local.kongyu.MyEasyExcel.utils.ZipUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/zip")
public class ZipController {

    @GetMapping("/exportWordsAsZip")
    public ResponseEntity<InputStreamResource> exportWordsAsZip() throws IOException, InterruptedException, ExecutionException {
        Path tempDir = Files.createTempDirectory("word_export");

        try {
            // 准备内容列表
            List<String> contents = Arrays.asList("Word Content 1", "Word Content 2", "Word Content 3");

            // 使用多线程生成Word文件
            List<String> wordFilePaths = WordUtil.createWordsConcurrently(contents, tempDir);

            // 打包生成的Word文件为zip
            String zipFilePath = "1.zip";
            ZipUtil.createZip(wordFilePaths.toArray(new String[0]), zipFilePath);
            File zipFile = new File(zipFilePath);

            // 下载zip文件
            InputStreamResource resource = new InputStreamResource(new FileInputStream(zipFile));
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + zipFile.getName());

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(zipFile.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } finally {
            // 清理临时目录及文件
            Files.walk(tempDir)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
    }
}

