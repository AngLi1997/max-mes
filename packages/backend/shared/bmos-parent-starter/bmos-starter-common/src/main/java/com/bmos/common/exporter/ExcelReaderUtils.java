package com.bmos.common.exporter;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.analysis.ExcelAnalyser;
import com.alibaba.excel.analysis.ExcelReadExecutor;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.common.exporter.listener.DefaultHeadCheckListener;
import com.bmos.common.exporter.util.ExcelI18nUtil;
import com.bmos.common.response.ResponseItem;

import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * excel阅读器实用程序
 *
 * @author yigaohui
 * @date 2023/11/16
 */

public class ExcelReaderUtils extends EasyExcelFactory {

    /**
     * 阅读
     *
     * @param inputStream 输入流
     * @param sheetName   图纸名称
     * @return @return {@link List }<{@link ? }>
     * @author yigaohui
     * @date 2023/11/16
     */

    public static List<?> read(InputStream inputStream, String sheetName) {
        ExcelReaderSheetBuilder sheetBuilder = EasyExcel.read(inputStream).sheet(sheetName);
        validImportTemplate(sheetName, sheetBuilder);
        return sheetBuilder.doReadSync();
    }

    /**
     * 阅读
     *
     * @param inputStream   输入流
     * @param head          头
     * @param dataSheetName 数据表名称
     * @return @return {@link List }<{@link T }>
     * @author yigaohui
     * @date 2023/11/16
     */

    public static <T> List<T> read(InputStream inputStream, Class<T> head, String dataSheetName) {
        if (StrUtil.isEmpty(dataSheetName)) {
            throw new RuntimeException("sheet data can not be null");
        }
        dataSheetName = ExcelI18nUtil.getI18n(dataSheetName);
        ExcelReaderSheetBuilder sheetBuilder =
                EasyExcel.read(inputStream, head, new DefaultHeadCheckListener(head)).sheet(dataSheetName);
        validImportTemplate(dataSheetName, sheetBuilder);

        return sheetBuilder.doReadSync();
    }

    /**
     * 判断是否使用有效的模板导入
     *
     * @param dataSheetName 数据表名称
     * @param sheetBuilder  图纸生成器
     * @return
     * @author yigaohui
     * @date 2023/11/16
     */

    private static void validImportTemplate(String dataSheetName, ExcelReaderSheetBuilder sheetBuilder) {
        ExcelReader excelReader = (ExcelReader) ReflectUtil.getFieldValue(sheetBuilder, "excelReader");
        ExcelAnalyser excelAnalyser = (ExcelAnalyser) ReflectUtil.getFieldValue(excelReader, "excelAnalyser");
        ExcelReadExecutor excelReadExecutor =
                (ExcelReadExecutor) ReflectUtil.getFieldValue(excelAnalyser, "excelReadExecutor");
        List<ReadSheet> readSheets = excelReadExecutor.sheetList();
        List<String> sheetNames = readSheets.stream().map(ReadSheet::getSheetName).collect(Collectors.toList());
        if (!sheetNames.contains(dataSheetName)) {
            throw new BmosException(BaseResponseCode.EXPORT_TEMPLATE_HEADER_ERROR);
        }
    }

    /**
     * 阅读
     *
     * @param inputStream   输入流
     * @param head          头
     * @param dataSheetName 数据表名称
     * @param readListener  读取侦听器
     * @return @return {@link List }<{@link T }>
     * @author yigaohui
     * @date 2023/11/16
     */

    public static <T> List<T> read(InputStream inputStream, Class<T> head, String dataSheetName,
                                   ReadListener<T> readListener) {
        if (StrUtil.isEmpty(dataSheetName)) {
            throw new RuntimeException("sheet data can not be null");
        }

        ExcelReaderSheetBuilder sheetBuilder = EasyExcel.read(inputStream, head, new DefaultHeadCheckListener(head))
                .registerReadListener(readListener).sheet(dataSheetName);
        validImportTemplate(dataSheetName, sheetBuilder);
        return sheetBuilder.doReadSync();
    }

}
