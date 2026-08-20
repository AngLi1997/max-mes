package com.bmos.mes.service.dataset.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.bmos.mes.service.dataset.common.DatasetTransValueData;
import com.bmos.mes.service.dataset.common.enums.DatasetTransValueDataType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.*;
import org.apache.xmlbeans.XmlCursor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * xlsx渲染工具
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/22 09:43
 */
public class XlsxRenderUtil {

    private static final Logger log = LoggerFactory.getLogger(XlsxRenderUtil.class);

    private static final String LOG_PREFIX = "[XlsxUtil]";

    private static final Pattern CELL_POSITION = Pattern.compile("([A-Z]+)(\\d+)");

    private static final Pattern CELL_AREA_POSITION = Pattern.compile("([A-Z]+)(\\d+):([A-Z]+)(\\d+)");

    public static Workbook fillValue(File file,
                                          Map<String, DatasetTransValueData> params,
                                          Map<String, List<CellPosition>> positions, String emptyPlaceholder) throws Exception {
        Workbook workbook;
        if (file.getName().endsWith("xls")){
            workbook = new HSSFWorkbook(Files.newInputStream(file.toPath()));
        }else {
            workbook = new XSSFWorkbook(file);
        }
        Sheet sheet = workbook.getSheetAt(0);
        if (workbook instanceof XSSFWorkbook){
            XSSFSheet xssfSheet = (XSSFSheet) sheet;
            List<XSSFShape> shapes = xssfSheet.createDrawingPatriarch().getShapes();
            XSSFDrawing drawingPatriarch = xssfSheet.getDrawingPatriarch();
            // 处理图片
            for (XSSFShape shape : shapes) {
                if (shape instanceof XSSFPicture) {
                    XSSFPicture xssfPicture = (XSSFPicture) shape;
                    // 获取占位图片锚点
                    XSSFClientAnchor clientAnchor = xssfPicture.getClientAnchor();
                    String desc = xssfPicture.getCTPicture().getNvPicPr().getCNvPr().getDescr();
                    if (StrUtil.isBlank(desc)) {
                        continue;
                    }
                    Matcher matcher = PlaceholderConstants.PATTERN.matcher(desc);
                    if (!matcher.find()) {
                        continue;
                    }
                    String placeholder = matcher.group();
                    if (params.containsKey(placeholder)) {
                        DatasetTransValueData valueData = params.get(placeholder);
                        if (valueData.getType() == DatasetTransValueDataType.IMAGE) {
                            String urls = valueData.getValue();
                            String[] split = urls.split(",");
                            int lineCount = 0;
                            for (String url : split) {
                                byte[] bytes = ImageUtil.downloadImage(url);
                                // 新图片下标
                                int position = workbook.addPicture(bytes, xssfPicture.getPictureData().getPictureType());
                                // 生成替换的图片
                                drawingPatriarch.createPicture(positionYAdd(clientAnchor, lineCount++), position);
                            }
                            // 删除占位的图片
                            deletePicture(xssfPicture);
                        }
                    }
                }
            }
        }
        LinkedHashMap<CellPosition, String> replaceMap = new LinkedHashMap<>();
        for (Map.Entry<String, List<CellPosition>> stringListEntry : positions.entrySet()) {
            for (CellPosition cellPosition : stringListEntry.getValue()) {
                replaceMap.put(cellPosition, stringListEntry.getKey());
            }
        }
        ArrayList<CellPosition> positionArrayList = new ArrayList<>(replaceMap.keySet());
        positionArrayList.sort(Comparator.comparingInt(CellPosition::getRow));
        LinkedHashMap<CellPosition, String> sortMap = new LinkedHashMap<>();
        for (CellPosition cellPosition : positionArrayList) {
            sortMap.put(cellPosition, replaceMap.get(cellPosition));
        }
        for (CellPosition cellPosition : positionArrayList) {
            String placeholder = sortMap.get(cellPosition);
            Cell cell = sheet.getRow(cellPosition.getRow()).getCell(cellPosition.getColumn());
            if (cell == null) {
                continue;
            }
            String templateValue = Optional.of(cell)
                    .map(Cell::getStringCellValue)
                    .orElse(null);
            if (StrUtil.isBlank(templateValue)) {
                continue;
            }
            if (params.containsKey(placeholder)) {
                DatasetTransValueData valueData = params.get(placeholder);
                if (valueData == null || StrUtil.isBlank(valueData.getValue())) {
                    templateValue = StringUtils.replace(templateValue, placeholder, emptyPlaceholder);
                } else if (Objects.equals(valueData.getType(), DatasetTransValueDataType.TEXT)) {
                    templateValue = StringUtils.replace(templateValue, placeholder, valueData.getValue());
                }else if (Objects.equals(valueData.getType(), DatasetTransValueDataType.CHECKBOX)) {
                    templateValue = StringUtils.replace(templateValue, placeholder, valueData.getValue());
                } else {
                    // 类型不是文本
                    templateValue = StringUtils.replace(templateValue, placeholder, emptyPlaceholder);
                }
            } else {
                // 未找到匹配项
                templateValue = StringUtils.replace(templateValue, placeholder, emptyPlaceholder);
            }
            cell.setCellValue(templateValue);
        }
        return workbook;
    }

    public static Workbook fillValue(File file,
                                          Map<String, DatasetTransValueData> params, String emptyPlaceholder) throws Exception {
        Map<String, List<CellPosition>> positions = scanPlaceHolder(file);
        return fillValue(file, params, positions, emptyPlaceholder);
    }

    /**
     * 删除图片
     *
     * @param xssfPicture 图片
     */
    private static void deletePicture(XSSFPicture xssfPicture) {
        XSSFDrawing drawing = xssfPicture.getDrawing();
        XmlCursor cursor = xssfPicture.getCTPicture().newCursor();
        cursor.toParent();
        if (cursor.getObject() instanceof org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTTwoCellAnchor) {
            for (int i = 0; i < drawing.getCTDrawing().getTwoCellAnchorList().size(); i++) {
                if (cursor.getObject().equals(drawing.getCTDrawing().getTwoCellAnchorArray(i))) {
                    drawing.getCTDrawing().removeTwoCellAnchor(i);
                }
            }
        } else if (cursor.getObject() instanceof org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTOneCellAnchor) {
            for (int i = 0; i < drawing.getCTDrawing().getOneCellAnchorList().size(); i++) {
                if (cursor.getObject().equals(drawing.getCTDrawing().getOneCellAnchorArray(i))) {
                    drawing.getCTDrawing().removeOneCellAnchor(i);
                }
            }
        } else if (cursor.getObject() instanceof org.openxmlformats.schemas.drawingml.x2006.spreadsheetDrawing.CTAbsoluteAnchor) {
            for (int i = 0; i < drawing.getCTDrawing().getAbsoluteAnchorList().size(); i++) {
                if (cursor.getObject().equals(drawing.getCTDrawing().getAbsoluteAnchorArray(i))) {
                    drawing.getCTDrawing().removeAbsoluteAnchor(i);
                }
            }
        }
    }

    /**
     * 计算插入图片锚点
     *
     * @param oldAnchor 原锚点
     * @param lineCount 行数
     * @return 新锚点
     */
    private static XSSFClientAnchor positionYAdd(XSSFClientAnchor oldAnchor, int lineCount) {
        // 向下移动1个单元格
        XSSFClientAnchor xssfClientAnchor = new XSSFClientAnchor(
                oldAnchor.getDx1(),
                oldAnchor.getDy1(),
                oldAnchor.getDx2(),
                oldAnchor.getDy2(),
                oldAnchor.getCol1(),
                oldAnchor.getRow1() + lineCount,
                oldAnchor.getCol2(),
                oldAnchor.getRow2() + lineCount);
        xssfClientAnchor.setAnchorType(oldAnchor.getAnchorType());
        return xssfClientAnchor;
    }

    /**
     * 扫描占位符
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static Map<String, List<CellPosition>> scanPlaceHolder(File file) throws Exception {
        log.info("{} 开始扫描excel,解析所有占位符", LOG_PREFIX);
        Map<String, List<CellPosition>> result = new HashMap<>();
        EasyExcel.read(file, null, new ReadListener<LinkedHashMap<Integer, String>>() {
                    @Override
                    public void invoke(LinkedHashMap<Integer, String> map, AnalysisContext analysisContext) {
                        for (Map.Entry<Integer, String> entry : map.entrySet()) {
                            if (StrUtil.isBlank(entry.getValue())) {
                                continue;
                            }
                            Matcher matcher = PlaceholderConstants.PATTERN.matcher(entry.getValue());
                            while (matcher.find()) {
                                String placeholder = matcher.group();
                                List<CellPosition> positions = result.computeIfAbsent(placeholder, k -> new ArrayList<>());
                                positions.add(new CellPosition(analysisContext.readRowHolder().getRowIndex(), entry.getKey()));
                            }
                        }
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                        log.info("{} 扫描excel结束,占位符数据:", LOG_PREFIX);
                        result.forEach((k, v) -> {
                            log.info("{} -> {}", k, v);
                        });
                    }
                })
                .headRowNumber(0)
                .sheet(0)
                .doRead();
        return result;
    }

    /**
     * 单元格定位
     */
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CellPosition {

        /**
         * 行
         */
        private Integer row;

        /**
         * 列
         */
        private Integer column;
    }

    public static AreaPosition getCellAreaPosition(String position) {
        Matcher matcher = CELL_AREA_POSITION.matcher(position);
        while (matcher.find()) {
            return new AreaPosition(
                    Integer.parseInt(matcher.group(2)),
                    columnLetterToNumber(matcher.group(1)),
                    Integer.parseInt(matcher.group(4)),
                    columnLetterToNumber(matcher.group(3)));
        }
        return null;
    }

    public static Position getCellPosition(String position) {
        Matcher matcher = CELL_POSITION.matcher(position);
        while (matcher.find()) {
            return new Position(columnLetterToNumber(matcher.group(1)), Integer.parseInt(matcher.group(2)));
        }
        return null;
    }

    /**
     * 获取sheet中的某个区域
     *
     * @param sheet
     * @param areaPosition 区域定位
     * @return
     */
    private static XSSFCell[][] getCellArea(XSSFSheet sheet, AreaPosition areaPosition) {
        int rows = areaPosition.endRow - areaPosition.startRow + 1;
        int cols = areaPosition.endCol - areaPosition.startCol + 1;
        XSSFCell[][] result = new XSSFCell[rows][cols];
        int k = 0;
        for (int i = areaPosition.startRow; i <= areaPosition.endRow; i++) {
            int l = 0;
            for (int j = areaPosition.startCol; j <= areaPosition.endCol; j++) {
                result[k][l++] = sheet.getRow(i - 1).getCell(j - 1);
            }
            k++;
        }
        return result;
    }


    public static SXSSFWorkbook linkExcel(File sourceFile, String sourcePosition, File targetFile, String targetPosition) throws Exception {
        XSSFWorkbook source = new XSSFWorkbook(sourceFile);
        XSSFSheet sheet = source.getSheetAt(0);
        AreaPosition cellAreaPosition = getCellAreaPosition(sourcePosition);
        XSSFCell[][] cellArea = getCellArea(sheet, cellAreaPosition);
        for (int i = 0; i < cellArea.length; i++) {
            for (int j = 0; j < cellArea[i].length; j++) {
                XSSFRow row = sheet.getRow(i);
                if (row == null) {
                    row = sheet.createRow(i);
                }
                CTCell ctCell = cellArea[i][j].getCTCell();
                if (ctCell != null) {
                    row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).setCTCell(ctCell);
                }
            }
        }
        return new SXSSFWorkbook(source);
    }

    private static int columnLetterToNumber(String column) {
        int result = 0;
        for (int i = 0; i < column.length(); i++) {
            result *= 26;
            result += column.charAt(i) - 'A' + 1;
        }
        return result;
    }

    @Data
    @AllArgsConstructor
    private static class Position {

        private int row;

        private int column;
    }

    @Data
    @AllArgsConstructor
    private static class AreaPosition {

        private int startRow;

        private int startCol;

        private int endRow;

        private int endCol;
    }
}
