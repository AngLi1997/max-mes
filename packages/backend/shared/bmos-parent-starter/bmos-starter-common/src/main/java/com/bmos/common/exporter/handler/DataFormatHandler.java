package com.bmos.common.exporter.handler;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.DataFormatData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.bmos.common.exporter.annotation.DiExcelDataFormat;
import com.bmos.common.exporter.bo.SheetDataBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.DefaultIndexedColorMap;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author : yigaohui
 * @version : 1.0
 */
@Slf4j
public class DataFormatHandler implements SheetWriteHandler, CellWriteHandler {

    /**
     * SheetDataBo 数据
     */
    private Map<String, SheetDataBo> sheetDataBoMp = new HashMap<>();
    private Map<String, Map<Integer, CellStyle>> columeStyle = new HashMap<>();
    private Map<String, Map<Integer, DataFormatData>> dataFormatDataMap = new HashMap<>();

    public DataFormatHandler(List<SheetDataBo> sheetDataBoList) {
        if (!CollectionUtils.isEmpty(sheetDataBoList)) {
            for (SheetDataBo sheetDataBo : sheetDataBoList) {
                sheetDataBoMp.put(sheetDataBo.getSheetName(), sheetDataBo);
            }
        }

    }

    private static XSSFColor titleColor = new XSSFColor(new java.awt.Color(192, 192, 192), new DefaultIndexedColorMap());

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Sheet sheet = writeSheetHolder.getCachedSheet();
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        SheetDataBo sheetDataBo = sheetDataBoMp.get(sheet.getSheetName());
        if (sheetDataBo == null) {
            return;
        }
        Map<Integer, Short> columnDataFormatMp = createFieldDataDormat(workbook.createDataFormat(), sheetDataBo.getHead());
        //跳过没有校验的sheet页
        if (CollectionUtil.isEmpty(columnDataFormatMp)) {
            return;
        }
        //给每个单元格设置校验
        for (Map.Entry<Integer, Short> entry : columnDataFormatMp.entrySet()) {
            XSSFCellStyle style = (XSSFCellStyle) columeStyle.computeIfAbsent(sheetDataBo.getSheetName(), (n) -> new HashMap<>()).computeIfAbsent(entry.getKey(), (k) -> {
                CellStyle newStyle = workbook.createCellStyle();
                newStyle.setDataFormat(entry.getValue());
                return newStyle;
            });
            /*style.setFillForegroundColor(titleColor);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
            style.setDataFormat(entry.getValue());
            sheet.setDefaultColumnStyle(entry.getKey(), style);
        }

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder,
                                 List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer relativeRowIndex, Boolean isHead) {
        if (isHead || sheetDataBoMp.isEmpty()) {
            return;
        }
        Sheet sheet = writeSheetHolder.getSheet();
        Workbook workbook = sheet.getWorkbook();
        SheetDataBo sheetDataBo = sheetDataBoMp.get(sheet.getSheetName());
        if (sheetDataBo == null) {
            return;
        }
        Map<Integer, Short> columnDataDormatMp = createFieldDataDormat(workbook.createDataFormat(), sheetDataBo.getHead());
        //跳过没有校验的sheet页
        if (CollectionUtil.isEmpty(columnDataDormatMp)) {
            return;
        }
        int columnIndex = cell.getColumnIndex();
        Map<Integer, String> columnIndexFormatStrMp = getClassFieldDataFormatMap(sheetDataBo.getHead());
        if (columnDataDormatMp.containsKey(columnIndex)) {
            DataFormatData dataFormatData = dataFormatDataMap.computeIfAbsent(sheetDataBo.getSheetName(), (k) -> new HashMap<>())
                    .computeIfAbsent(columnIndex, (k) -> {
                        DataFormatData d = new DataFormatData();
                        d.setIndex(columnDataDormatMp.get(cell.getColumnIndex()));
                        d.setFormat(columnIndexFormatStrMp.get(cell.getColumnIndex()));
                        return d;
                    });
            cellDataList.get(0).getOrCreateStyle().setDataFormatData(dataFormatData);
        }
    }

    /**
     * 实体列数据校验的缓存
     */
    private static WeakHashMap<Class<?>, Map<Integer, String>> _classDataFormatMpCache = new WeakHashMap<>();

    /**
     * 获取对象 数据列校验 的dataformat的 索引
     * key:excel列号 value:校验的dataforamt序号
     *
     * @param dataFormat
     * @param clazz
     * @return
     */
    private Map<Integer, Short> createFieldDataDormat(DataFormat dataFormat, Class<?> clazz) {
        if (clazz == null) {
            return Collections.EMPTY_MAP;
        }
        Map<Integer, Short> columnFormatIndex = new HashMap<>();
        for (Map.Entry<Integer, String> entry : getClassFieldDataFormatMap(clazz).entrySet()) {
            columnFormatIndex.put(entry.getKey(), dataFormat.getFormat(entry.getValue()));
        }
        return columnFormatIndex;
    }

    /**
     * 获取实体类的数据校验map,key:excel列号 value:校验自定义字符
     *
     * @param clazz
     * @return
     */
    private Map<Integer, String> getClassFieldDataFormatMap(Class<?> clazz) {
        synchronized (clazz) {
            if (_classDataFormatMpCache.containsKey(clazz)) {
                return _classDataFormatMpCache.get(clazz);
            }
            Map<Integer, String> rs = new HashMap<>();
            for (Field field : clazz.getDeclaredFields()) {
                DiExcelDataFormat dataFormatAn = field.getAnnotation(DiExcelDataFormat.class);
                ExcelProperty propertyAn = field.getAnnotation(ExcelProperty.class);
                if (dataFormatAn == null) {
                    continue;
                }
                if (propertyAn == null || propertyAn.index() < 0) {
                    log.warn("实体:{} 属性:{} 列号:{} 校验格式:{} 未指定列号，跳过", clazz.getSimpleName(), field.getName(), propertyAn.index(), dataFormatAn.value());
                    continue;
                }
                rs.put(propertyAn.index(), dataFormatAn.value());
            }
            if (clazz.getSuperclass() != Object.class) {
                rs.putAll(getClassFieldDataFormatMap(clazz.getSuperclass()));
            }
            return rs;
        }

    }
}
