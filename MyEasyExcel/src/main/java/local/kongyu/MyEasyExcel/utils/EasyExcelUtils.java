package local.kongyu.MyEasyExcel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * EasyExcel工具类
 *
 * @author 孔余
 * @since 2024-02-06 09:27:51
 */
public class EasyExcelUtils {

    /**
     * 写入Excel
     *
     * @param response             浏览器响应
     * @param easyExcelExportParam excel的参数
     */
    public static void writeExcel(HttpServletResponse response, EasyExcelExportParam easyExcelExportParam) {
        //获取输出流
        try (ServletOutputStream outputStream = installResponse(response, easyExcelExportParam)) {
            List<?> list = easyExcelExportParam.getData();
            String sheetName = easyExcelExportParam.getSheetName();
            boolean ifNote = easyExcelExportParam.isOpenNote();
            ExcelTypeEnum excelTypeEnum = easyExcelExportParam.getFileType();
            Class<?> aClass;
            //导出的数据为空就是导出模板,只有表头
            if (ObjectUtils.isEmpty(list)) {
                aClass = getGenericClass(list);
                if (ifNote) {
                    EasyExcel.write(outputStream, aClass).head(aClass).excelType(excelTypeEnum).
                            sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
                } else {
                    EasyExcel.write(outputStream).head(easyExcelExportParam.getHeadList()).excelType(excelTypeEnum).
                            sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
                }
                return;
            }
            aClass = list.get(0).getClass();
            //导出数据
            if (ifNote) {
                EasyExcel.write(outputStream, aClass).excelType(excelTypeEnum).sheet(sheetName).
                        registerWriteHandler(installCellStyleStrategy()).doWrite(list);
            } else {
                EasyExcel.write(outputStream, aClass).head(easyExcelExportParam.getHeadList()).excelType(excelTypeEnum).
                        sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入CSV
     *
     * @param response             浏览器响应
     * @param easyExcelExportParam excel的参数
     */
    public static void writeCsv(HttpServletResponse response, EasyExcelExportParam easyExcelExportParam) {
        // 设置文件类型
        easyExcelExportParam.setFileType(ExcelTypeEnum.CSV);
        //获取输出流
        try (ServletOutputStream outputStream = installResponse(response, easyExcelExportParam)) {
            List<?> list = easyExcelExportParam.getData();
            String sheetName = easyExcelExportParam.getSheetName();
            boolean ifNote = easyExcelExportParam.isOpenNote();

            ExcelTypeEnum excelTypeEnum = ExcelTypeEnum.CSV;
            Class<?> aClass;
            //导出的数据为空就是导出模板,只有表头
            if (ObjectUtils.isEmpty(list)) {
                aClass = getGenericClass(list);
                if (ifNote) {
                    EasyExcel.write(outputStream, aClass).head(aClass).excelType(excelTypeEnum).
                            sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
                } else {
                    EasyExcel.write(outputStream).head(easyExcelExportParam.getHeadList()).excelType(excelTypeEnum).
                            sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
                }
                return;
            }
            aClass = list.get(0).getClass();
            //导出数据
            if (ifNote) {
                EasyExcel.write(outputStream, aClass).excelType(excelTypeEnum).sheet(sheetName).
                        registerWriteHandler(installCellStyleStrategy()).doWrite(list);
            } else {
                EasyExcel.write(outputStream, aClass).head(easyExcelExportParam.getHeadList()).excelType(excelTypeEnum).
                        sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
            }
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 根据模板填充列表数据
     *
     * @param response             浏览器响应
     * @param easyExcelExportParam excel的参数
     * @param file                 ClassPathResource resources下的文件位置
     */
    public static void writeExcelListFill(HttpServletResponse response, EasyExcelExportParam easyExcelExportParam, String file) {
        //获取输出流
        try (ServletOutputStream outputStream = installResponse(response, easyExcelExportParam)) {
            List<?> list = easyExcelExportParam.getData();
            String sheetName = easyExcelExportParam.getSheetName();
            ExcelTypeEnum excelTypeEnum = easyExcelExportParam.getFileType();
            InputStream inputStream = new ClassPathResource(file).getInputStream();
            EasyExcel.write(outputStream).excelType(excelTypeEnum)
                    .withTemplate(inputStream)
                    .sheet(sheetName).doFill(list);
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写入Excel到本地目录文件
     *
     * @param pathName             本地目录文件
     * @param easyExcelExportParam excel的参数
     */
    public static void writeExcel(String pathName, EasyExcelExportParam easyExcelExportParam) {
        try {
            //获取输出流
            List<?> list = easyExcelExportParam.getData();
            String sheetName = easyExcelExportParam.getSheetName();
            boolean ifNote = easyExcelExportParam.isOpenNote();
            ExcelTypeEnum excelTypeEnum = easyExcelExportParam.getFileType();
            Class<?> aClass;
            //导出的数据为空就是导出模板,只有表头
            if (ObjectUtils.isEmpty(list)) {
                aClass = getGenericClass(list);
                if (ifNote) {
                    EasyExcel.write(pathName, aClass).head(aClass).excelType(excelTypeEnum).
                            sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
                } else {
                    EasyExcel.write(pathName).head(easyExcelExportParam.getHeadList()).excelType(excelTypeEnum).
                            sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
                }
                return;
            }
            aClass = list.get(0).getClass();
            //导出数据
            if (ifNote) {
                EasyExcel.write(pathName, aClass).excelType(excelTypeEnum).sheet(sheetName).
                        registerWriteHandler(installCellStyleStrategy()).doWrite(list);
            } else {
                EasyExcel.write(pathName, aClass).head(easyExcelExportParam.getHeadList()).excelType(excelTypeEnum).
                        sheet(sheetName).registerWriteHandler(installCellStyleStrategy()).doWrite(list);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 得到集合里面泛型的Class对象
     *
     * @param object 对象
     * @return Class<?>
     */
    private static Class<?> getGenericClass(Object object) {
        Type clazz = object.getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) clazz;
        return (Class<?>) pt.getActualTypeArguments()[0];
    }

    /**
     * 写入Excel多个Sheet模式
     *
     * @param response             浏览器响应
     * @param easyExcelExportParam excel的参数
     */
    public static void writeExcelGreatSheet(HttpServletResponse response, EasyExcelExportParam easyExcelExportParam) {
        //获取输出流
        ExcelWriter excelWriter = null;
        try (ServletOutputStream outputStream = installResponse(response, easyExcelExportParam)) {
            List<List<?>> dataList = easyExcelExportParam.getDataList();
            Set<String> sheetNameSet = easyExcelExportParam.getSheetNameSetGreatSheet();
            if (ObjectUtils.isEmpty(sheetNameSet)) {
                throw new RuntimeException("工作表名称不能为空");
            }
            excelWriter = EasyExcel.write(outputStream).build();
            if (dataList.size() != sheetNameSet.size()) {
                throw new RuntimeException("工作表数据和工作表名称个数不匹配");
            }
            boolean openNote = easyExcelExportParam.isOpenNote();
            int i = 0;
            //数据为空的情况
            if (ObjectUtils.isEmpty(dataList.get(i))) {
                excelWriter = EasyExcel.write(outputStream).build();
                for (String sheetName : sheetNameSet) {
                    WriteSheet writeSheet;
                    if (openNote) {
                        Class<?> aClass = getGenericClass(dataList.get(i++));
                        writeSheet = EasyExcel.writerSheet(sheetName).head(aClass).
                                registerWriteHandler(installCellStyleStrategy()).build();
                    } else {
                        writeSheet = EasyExcel.writerSheet(sheetName).head(easyExcelExportParam.getHeadListGreatSheet().get(i)).
                                registerWriteHandler(installCellStyleStrategy()).build();
                    }
                    excelWriter.write(dataList, writeSheet);
                }
                excelWriter.finish();
                return;
            }
            for (String sheetName : sheetNameSet) {
                List<?> list = dataList.get(i);
                //导出
                WriteSheet writeSheet;
                if (openNote) {
                    writeSheet = EasyExcel.writerSheet(sheetName).head(list.get(0).getClass()).
                            registerWriteHandler(installCellStyleStrategy()).build();
                } else {
                    writeSheet = EasyExcel.writerSheet(sheetName).head(easyExcelExportParam.getHeadListGreatSheet().get(i)).
                            registerWriteHandler(installCellStyleStrategy()).build();
                }
                excelWriter.write(list, writeSheet);
                i++;
            }
            excelWriter.finish();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * Excel读取所有sheet
     *
     * @param file HTTP文件
     * @param head 对应实体类
     * @param <T>
     * @return 实体类列表
     * @throws IOException
     */
    public static <T> List<T> readExcelAllSheet(MultipartFile file, Class<T> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null).doReadAllSync();
    }

    /**
     * Excel读取单个sheet
     *
     * @param file
     * @param head
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> readExcelOneSheet(MultipartFile file, final Class<?> head) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, null).doReadAllSync();
    }

    /**
     * 补充表头
     *
     * @param headList 表头集合
     * @return List<String>
     */
    public static List<String> replenishHead(List<String> headList) {
        List<String> heads = new ArrayList<>();
        String head = "";
        for (String e : headList) {
            if (StringUtils.isNotBlank(e)) {
                head = e;
            }
            heads.add(head);
        }
        return heads;
    }

    /**
     * 旋转表头(因为EasyExcel的导出表头有点反人类所以采用该方法旋转表头方便理解)
     * 通过该方法你可以理解为一个List<String> 就是第一层表头
     * 再来一个List<String>就是第二层表头
     *
     * @param headFields 表头数据
     * @return List<List < String>>
     */
    public static List<List<String>> rotateHead(List<List<String>> headFields) {
        List<List<String>> result = new ArrayList<>();
        for (List<String> row : headFields) {
            for (int i = 0; i < row.size(); i++) {
                if (result.size() > i) {
                    // 往对应第i个List<String> 添加添加值
                    result.get(i).add(row.get(i));
                } else {
                    // 分割成单个List<String>
                    result.add(new ArrayList<>(Collections.singletonList(row.get(i))));
                }
            }
        }
        return result;
    }

    /**
     * 设置响应格式避免出现文件乱码
     *
     * @param response             setResponse
     * @param easyExcelExportParam excel的参数
     * @return ServletOutputStream 输出流
     * @throws IOException IO流异常
     */
    private static ServletOutputStream installResponse(HttpServletResponse response, EasyExcelExportParam easyExcelExportParam) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码,所有通过后端的文件下载都可以如此处理
        String fileName = URLEncoder.encode(easyExcelExportParam.getExcelName(), "UTF-8").replaceAll("\\+", "%20");
        //建议加上该段,否则可能会出现前端无法获取Content-disposition
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + easyExcelExportParam.getFileType().getValue());
        return response.getOutputStream();
    }

    /**
     * 设置内容和表头的样式策略
     * 可以自行修改满足自己要求的样式
     *
     * @return HorizontalCellStyleStrategy
     */
    private static HorizontalCellStyleStrategy installCellStyleStrategy() {
        //内容样式策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        //垂直居中,水平居中
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        setBorderStyleAndFont(contentWriteCellStyle);
        //设置自动换行
        contentWriteCellStyle.setWrapped(true);
        //头策略使用默认 设置字体大小
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        setBorderStyleAndFont(headWriteCellStyle);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    /**
     * 设置边框样式和字体大小
     *
     * @param writeCellStyle 写入样式
     */
    private static void setBorderStyleAndFont(WriteCellStyle writeCellStyle) {
        //边框样式
        writeCellStyle.setBorderLeft(BorderStyle.MEDIUM);
        writeCellStyle.setBorderTop(BorderStyle.MEDIUM);
        writeCellStyle.setBorderRight(BorderStyle.MEDIUM);
        writeCellStyle.setBorderBottom(BorderStyle.MEDIUM);
        //字体策略
        WriteFont contentWriteFont = new WriteFont();
        //字体大小
        contentWriteFont.setFontHeightInPoints((short) 14);
        writeCellStyle.setWriteFont(contentWriteFont);
    }

}
