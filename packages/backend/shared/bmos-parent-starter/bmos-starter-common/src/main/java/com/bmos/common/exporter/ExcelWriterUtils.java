package com.bmos.common.exporter;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.util.StringUtils;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;

import com.bmos.common.exporter.bo.SheetDataBo;
import com.bmos.common.exporter.handler.DataFormatHandler;
import com.bmos.common.exporter.handler.ExcelOptionsHandler;
import com.bmos.common.exporter.handler.I18nCellWriteHandler;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;


import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author yigaohui
 * @date 2022/8/23 11:23
 */
@Slf4j
public class ExcelWriterUtils {
    /**
     * 导出excel到浏览器
     *
     * @param fileName     文件名
     * @param response     响应
     * @param sheetDataBos
     * @throws Exception
     */
    public static void write(String fileName, HttpServletResponse response, List<SheetDataBo> sheetDataBos, WriteHandler... customHandlers) throws Exception {
        setResponseMeta(response, fileName);
        write(response.getOutputStream(), sheetDataBos, customHandlers);
    }

    /**
     * @param outputStream
     * @param sheetDataBos
     * @Author: yigaohui
     * @Date: 2022/8/24 20:01
     * @Description: 比较统一的接口，调用方封装 List<SheetDataBo>
     * @return: void
     */
    public static void write(OutputStream outputStream, List<SheetDataBo> sheetDataBos, WriteHandler... customHandlers) {
        if (CollectionUtils.isEmpty(sheetDataBos)) {
            throw new RuntimeException("sheet data can not be null");
        }
        ExcelWriter excelWriter = null;
        try {
            ExcelWriterBuilder excelWriteBuilder = EasyExcel.write(outputStream);
            //设置字典值 sheet页处理器
            excelWriteBuilder.registerWriteHandler(new ExcelOptionsHandler(sheetDataBos));
            // 设置国际化处理器
            excelWriteBuilder.registerWriteHandler(new I18nCellWriteHandler());
            //数据格式样式处理
            excelWriteBuilder.registerWriteHandler(new DataFormatHandler(sheetDataBos));
            //添加自定义处理器
            if (customHandlers != null && customHandlers.length > 0) {
                for (WriteHandler customHandler : customHandlers) {
                    excelWriteBuilder.registerWriteHandler(customHandler);
                }
            }
            //data sheet
            excelWriter = excelWriteBuilder.build();
            for (SheetDataBo sheetDataBo : sheetDataBos) {
                String sheetName = ExcelI18nUtil.getI18n(sheetDataBo.getSheetName());
                if (StringUtils.isEmpty(sheetName)) {
                    throw new RuntimeException("SheetName can not be null");
                }
                ExcelWriterSheetBuilder sheetBuilder = EasyExcel.writerSheet(sheetName);
                //设置实体类
                sheetBuilder.head(sheetDataBo.getHead());
                //设置单元格自适应宽度
                sheetBuilder.registerWriteHandler(new LongestMatchColumnWidthStyleStrategy());
                WriteSheet writeSheet = sheetBuilder.build();
                excelWriter.write(sheetDataBo.getData(), writeSheet);
            }

        } catch (Exception e) {
            log.error("err:", e);
            throw new RuntimeException(e.getMessage());
        } finally {
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    private static void setResponseMeta(HttpServletResponse response, String fileName) throws Exception {
        if (StringUtils.isEmpty(fileName)) {
            throw new Exception("export fileName can not be null");
        }

        fileName = URLEncoder.encode(fileName, "utf-8");
        if (!fileName.endsWith("xlsx") && !fileName.endsWith("xls")) {
            fileName = fileName + ".xlsx";
        }
        if (fileName.endsWith("xls")) {
            fileName = fileName.replace("xls", "xlsx");
        }
        fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/ms-excel; charset=UTF-8");

        response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
    }
}
