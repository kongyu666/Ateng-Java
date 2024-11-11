package local.kongyu.MyEasyExcel.utils;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class WordUtil {

    public static String createWord(String content, Path tempDir) throws IOException {
        String filePath = tempDir.resolve("word_" + UUID.randomUUID().toString() + ".docx").toString();

        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText(content);

            try (FileOutputStream out = new FileOutputStream(filePath)) {
                document.write(out);
            }
        }
        return filePath;
    }

    public static List<String> createWordsConcurrently(List<String> contents, Path tempDir) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<String>> futures = new ArrayList<>();

        // 为每个内容创建一个Word文件任务
        for (String content : contents) {
            Callable<String> task = () -> createWord(content, tempDir);
            futures.add(executor.submit(task));
        }

        // 等待所有任务完成并收集结果文件路径
        List<String> filePaths = new ArrayList<>();
        for (Future<String> future : futures) {
            filePaths.add(future.get());
        }

        executor.shutdown();
        return filePaths;
    }
}

