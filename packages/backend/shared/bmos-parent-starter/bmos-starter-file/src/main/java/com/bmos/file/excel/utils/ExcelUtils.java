package com.bmos.file.excel.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Excel 工具类
 */
public class ExcelUtils {
    /**
     * 将列表以 Excel 响应给前端
     *
     * @param response 响应
     * @param filename 文件名
     * @param sheetName Excel sheet 名
     * @param head Excel head 头
     * @param data 数据列表哦
     * @param <T> 泛型，保证 head 和 data 类型的一致性
     * @throws IOException 写入失败的情况
     */
    public static <T> void write(HttpServletResponse response, String filename, String sheetName,
                                 Class<T> head, List<T> data) throws IOException {
        // 输出 Excel
        EasyExcel.write(response.getOutputStream(), head)
            .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 基于 column 长度，自动适配。最大 255 宽度
            .sheet(sheetName).doWrite(data);
        // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + URLEncoder.encode(filename, StandardCharsets.UTF_8.toString()));
    }

    public static <T> void write(HttpServletResponse response, String filename,
          Class<T> head, Map<String, List<T>> data) throws IOException {
        ExcelWriter excelWriter = null;
        try {
            // 这里 需要指定写用哪个class去写
            excelWriter = EasyExcel.write(response.getOutputStream(), head)
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .build();
            AtomicInteger index = new AtomicInteger();
            for (Map.Entry<String, List<T>> entry : data.entrySet()) {
                WriteSheet writeSheet = EasyExcel.writerSheet(index.getAndIncrement(), entry.getKey())
                    .build();
                excelWriter.write(entry.getValue(), writeSheet);
            }
            // 设置 header 和 contentType。写在最后的原因是，避免报错时，响应 contentType 已经被修改了
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + filename);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        } finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    // 单行表头校验
    public static <T> List<T> read(MultipartFile file, Class<T> head) throws IOException {
       return EasyExcel.read(file.getInputStream(), head, new EasyExcelHeadListener(head))
                .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
                .doReadAllSync();
    }

    // 两行表头读取
    public static <T> List<T> read(MultipartFile file, Class<T> head, Integer headRowNumber) throws IOException {
        return EasyExcel.read(file.getInputStream(), head, new EasyExcelHeadListener(head))
            .headRowNumber(headRowNumber)
            .autoCloseStream(false)  // 不要自动关闭，交给 Servlet 自己处理
            .doReadAllSync();
    }
}
