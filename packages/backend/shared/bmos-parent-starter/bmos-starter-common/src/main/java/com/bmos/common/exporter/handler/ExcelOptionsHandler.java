package com.bmos.common.exporter.handler;

import com.alibaba.excel.write.handler.WorkbookWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import com.bmos.common.exporter.bo.OptionBo;
import com.bmos.common.exporter.bo.OptionsLocation;
import com.bmos.common.exporter.bo.SheetDataBo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.usermodel.*;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 设置表格的下拉选项
 * 字典值标签页
 * 第一行为 sheetName
 * 第二行为 optionName
 * 第三行及以后是数据行
 *
 * @author : yigaohui
 * @version : 1.0
 */
@Slf4j
public class ExcelOptionsHandler implements WorkbookWriteHandler {
    private final String dictionarySheetName = "数据字典";
    /**
     * excel 保护数据密码
     */
    private static final String passwd = System.getProperty("cestc.excel.passwd", "password");
    /**
     * 是否保护数据
     */
    private static final String protect = System.getProperty("cestc.excel.protect", "true");
    /**
     * 每个 sheet页选项对应的位置
     */

    private Map<String, List<OptionsLocation>> sheetOptionsMap = new HashMap<>();
    /**
     * sheet 页bo的map
     */
    private Map<String, SheetDataBo> sheetDataBoMap = new HashMap<>();
    /**
     * 初始化数据字典页的各列对应的 选项数据
     */

    private Map<Integer, OptionBo> optionsMap = new HashMap<>();
    /**
     * 字典页对应的列计数器
     */
    private AtomicInteger columnSize = new AtomicInteger(0);
    /**
     * 字典值页数据的 最大行数
     */
    private int maxOptionLength;
    /**
     * SheetDataBo 数据
     */
    private final List<SheetDataBo> sheetDataBoList;
    /**
     * 选项名称所在行
     */
    private final int optionNameRowNum = 1;
    /**
     * 字典值sheet数据
     */
    private LinkedList<Map<Integer, String>> dicDatas = new LinkedList<>();

    public ExcelOptionsHandler(List<SheetDataBo> sheetDataBoList) {

        this.sheetDataBoList = sheetDataBoList;
        initOptions(sheetDataBoList);
        initOptionsDatas();
    }

    /**
     * 初始化excel字典值sheet页需要的数据
     *
     * @param sheetDataBoList
     */
    private void initOptions(List<SheetDataBo> sheetDataBoList) {

        for (SheetDataBo sheetDataBo : sheetDataBoList) {
            String sheetName = sheetDataBo.getSheetName();
            List<OptionsLocation> sheetOptionLocations = new ArrayList<>();
            sheetOptionsMap.put(sheetName, sheetOptionLocations);
            sheetDataBoMap.put(sheetName, sheetDataBo);
            List<OptionBo> optionBos = sheetDataBo.getOptionBos();
            if (optionBos == null || optionBos.size() == 0) {
                continue;
            }
            OptionalInt max = optionBos.stream().mapToInt(v -> v.getOptions().size()).max();
            int optionsSize = max.orElse(0);
            maxOptionLength = Math.max(optionsSize, maxOptionLength);
            for (OptionBo optionBo : optionBos) {
                //该选择再字典值sheet页当中的列
                int dicCols = columnSize.getAndIncrement();
                OptionsLocation optionsLotion = new OptionsLocation(sheetName, optionBo, dicCols);
                sheetOptionLocations.add(optionsLotion);
                optionsMap.put(dicCols, optionBo);
            }
            //sheet页 字典值直接空一列
            columnSize.getAndIncrement();
        }
    }

    private void initOptionsDatas() {
        //合并各个sheet页的表头 列:数据
        Map<Integer, String> sheetName = new HashMap<>();
        Map<Integer, String> itemHead = new HashMap<>();
        dicDatas.add(sheetName);

        for (int col = 0; col < columnSize.get(); col++) {
            //sheet间隔列无数据
            if (!optionsMap.containsKey(col)) {
                continue;
            }
            itemHead.put(col, optionsMap.get(col).getOptionName());
            sheetName.put(col, "");
        }
        dicDatas.add(itemHead);
        for (int row = 0; row < maxOptionLength; row++) {
            Map<Integer, String> rowData = new HashMap<>();
            //sheet间隔列无数据
            for (int col = 0; col < columnSize.get(); col++) {
                //sheet间隔列无数据
                if (!optionsMap.containsKey(col)) {
                    continue;
                }
                OptionBo optionBo = optionsMap.get(col);
                if (row < optionBo.getOptions().size()) {
                    rowData.put(col, optionBo.getOptions().get(row));
                }
            }
            dicDatas.add(rowData);
        }
    }

    private static final char[] excelColArray = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 10进制的列数转换为excel的引用列字符(26进制 A~Z)
     *
     * @param colNum
     * @return
     */
    private static String toExcelColName(int colNum) {
        if (colNum < 26) {
            return "" + excelColArray[colNum];
        }
        StringBuilder result = new StringBuilder();
        while (colNum > 0) {
            result.insert(0, excelColArray[(colNum % 25) - 1]);
            colNum /= 26;
        }
        return result.toString();
    }

    /**
     * workbook创建完成之后设置 lie的选择框数据
     *
     * @param writeWorkbookHolder
     */
    @Override
    public void afterWorkbookDispose(WriteWorkbookHolder writeWorkbookHolder) {
        //没有下拉选项直接结束
        if (optionsMap.isEmpty()) {
            return;
        }
        Workbook workbook = writeWorkbookHolder.getCachedWorkbook();
        createDictionarySheet(workbook);
        CellStyle unlock = workbook.createCellStyle();
        CellStyle lock = workbook.createCellStyle();
        unlock.setLocked(false);
        lock.setLocked(true);
        //
        for (Map.Entry<String, List<OptionsLocation>> entry : sheetOptionsMap.entrySet()) {
            String sheetName = entry.getKey();
            Sheet sheet = workbook.getSheet(sheetName);
            for (OptionsLocation optionsLotion : entry.getValue()) {
                if (sheet instanceof SXSSFSheet) {
                    int dataColNum = optionsLotion.getOptionBo().getOptionCol();
                    //3 A1:A代表隐藏域创建第N列createCell(N)时。以A1列开始A行数据获取下拉数组
                    // format {sheetname}!{colNum}{rowStart}:{colNum}{rowEnd}
                    //默认选项从第三行开始，第一行是 shet页名称，第二行是标识当前列是字典值含义
                    String colRefName = toExcelColName(optionsLotion.getDicSheetCols());
                    //引用范围 行列直接一定要加 $ 符号，不加$符号 会自行推断加一
                    String dataRef = String.format("=%s!$%s$%d:$%s$%d", dictionarySheetName, colRefName, 3, colRefName, 2 + optionsLotion.getOptionBo().getOptions().size());
                    log.debug("sheet:{} cols:{} ref:{}", sheetName, optionsLotion.getOptionBo().getOptionCol(), dataRef);
                    int sheetDatSize = sheet.getLastRowNum() + 100;
                    DataValidationHelper helper = sheet.getDataValidationHelper();
                    DataValidationConstraint constraint = new XSSFDataValidationConstraint(DataValidationConstraint.ValidationType.LIST, dataRef);
                    CellRangeAddressList addressList = new CellRangeAddressList(BigInteger.ONE.intValue(), sheetDatSize, dataColNum, dataColNum);
                    DataValidation dataValidation = helper.createValidation(constraint, addressList);
                    if (dataValidation instanceof XSSFDataValidation) {
                        // 数据校验
                        dataValidation.setSuppressDropDownArrow(true);
                        dataValidation.setShowErrorBox(true);
                    } else {
                        dataValidation.setSuppressDropDownArrow(false);
                    }
                    // 作用在目标sheet上
                    sheet.addValidationData(dataValidation);

                }
            }
        }

        //保护列
        lockCol(workbook);
        log.debug("设置选项结束");

    }

    /**
     * 锁定excel列
     *
     * @param workbook
     */
    private void lockCol(Workbook workbook) {
        CellStyle lockedStyle = workbook.createCellStyle();
        lockedStyle.setLocked(true);
        CellStyle defaultStyle = workbook.createCellStyle();
        defaultStyle.setLocked(false);
        for (SheetDataBo sheetDataBo : sheetDataBoList) {
            if (sheetDataBo.getLockColumns() == null || sheetDataBo.getLockColumns().isEmpty()) {
                continue;
            }
            Sheet sheet = workbook.getSheet(sheetDataBo.getSheetName());
            short lastCellNum = sheet.getRow(0).getLastCellNum();
            for (int cols = 0; cols < lastCellNum; cols++) {
                sheet.setDefaultColumnStyle(cols, sheetDataBo.getLockColumns().contains(cols) ? lockedStyle : defaultStyle);
            }
            protectSheet(sheet);
        }
    }

    /**
     * 给weorkbook创建字典页
     * 里面放所有的字典值
     *
     * @param workbook
     */
    private void createDictionarySheet(Workbook workbook) {
        if (workbook.getSheet(dictionarySheetName) != null) {
            throw new RuntimeException("已经存在字典值 sheet页，请勿重复使用 " + getClass().getSimpleName());
        }

        Sheet dicSheet = workbook.createSheet(dictionarySheetName);
        for (Map.Entry<Integer, OptionBo> entry : optionsMap.entrySet()) {
            Integer cols = entry.getKey();
            OptionBo optionBo = entry.getValue();
            OptionalInt max = optionBo.getOptions().stream().mapToInt(v -> v.getBytes().length).max();
            int maxLength = Math.max(max.orElse(0), Optional.ofNullable(optionBo.getOptionName()).orElse("").getBytes().length);
            maxLength = Math.min(maxLength, 255);
            dicSheet.setColumnWidth(cols, maxLength * 256);
        }
        log.debug("数据字典数据:");
        for (int rows = 0; rows < dicDatas.size(); rows++) {
            Map<Integer, String> rowDatas = dicDatas.get(rows);
            Row row = dicSheet.createRow(rows);
            StringBuilder sb = null;
            if (log.isDebugEnabled()) {
                sb = new StringBuilder();
            }
            for (Map.Entry<Integer, String> entry : rowDatas.entrySet()) {
                Integer cols = entry.getKey();
                String item = rowDatas.get(cols);
                if (log.isDebugEnabled()) {
                    sb.append(item).append("，");
                }
                if (item != null) {
                    Cell cell = row.createCell(cols);
                    cell.setCellValue(rowDatas.get(cols));
                }
            }
            if (log.isDebugEnabled()) {
                log.debug(sb.toString());
            }
            //避免数据过多，easyExcel已经将数据落盘获取不到row导致空指针
            //所以在创建数据的时候直接设置
            if (rows == optionNameRowNum) {
                setOptionTitleStyle(workbook, row);
                setMergeCell(workbook, dicSheet);
            }
        }
        protectSheet(dicSheet);
    }

    /**
     * 保护sheet页
     *
     * @param sheet
     */
    void protectSheet(Sheet sheet) {
        if (Boolean.valueOf(protect)) {
            log.info("excel {} 开启了保护密码", sheet.getSheetName());
            sheet.protectSheet(passwd);
            if (sheet instanceof XSSFSheet) {
                ((XSSFSheet) sheet).enableLocking();
            }
        }
    }

    /**
     * 根据选项所属 sheet页合并单元格，并设置内容
     *
     * @param workbook
     * @param dicSheet
     */
    private void setMergeCell(Workbook workbook, Sheet dicSheet) {
        CellStyle mergeStyle = workbook.createCellStyle();
        mergeStyle.setAlignment(HorizontalAlignment.CENTER); // 创建一个居中格式
        //设置一个边框
        mergeStyle.setBorderTop(BorderStyle.THICK);
        for (Map.Entry<String, List<OptionsLocation>> entry : sheetOptionsMap.entrySet()) {
            List<OptionsLocation> locations = entry.getValue();
            if (locations == null || locations.isEmpty()) {
                continue;
            }
            String sheetName = entry.getKey();

            int max = locations.stream().mapToInt(v -> v.getDicSheetCols()).max().getAsInt();
            int min = locations.stream().mapToInt(v -> v.getDicSheetCols()).min().getAsInt();

            Cell cell = dicSheet.getRow(0).getCell(min);
            if (cell == null) {
                cell = dicSheet.getRow(0).createCell(min);
            }
            cell.setCellValue(sheetName + "(字典值)");
            cell.setCellStyle(mergeStyle);
            // bugfix:需要支持可选列单列的情况
            if (min == max) {
                return;
            }
            CellRangeAddress cra = new CellRangeAddress(0, 0, min, max);
            dicSheet.addMergedRegion(cra);
            RegionUtil.setBorderTop(BorderStyle.THICK, cra, dicSheet);
        }
    }

    private static XSSFColor titleColor = new XSSFColor(new java.awt.Color(192, 192, 192), new DefaultIndexedColorMap());

    /**
     * 设置选项列 头部描述的样式
     *
     * @param workbook
     * @param titleRow
     */
    private void setOptionTitleStyle(Workbook workbook, Row titleRow) {
        //设置选项描述样式
        XSSFCellStyle titleStyle = (XSSFCellStyle) workbook.createCellStyle();
        //#C0C0C0
        titleStyle.setFillForegroundColor(titleColor);
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setLocked(true);
        //水平剧中
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setBold(true);
        titleStyle.setFont(font);
        for (Integer col : dicDatas.get(1).keySet()) {
            //设置选择头样式
            titleRow.getCell(col).setCellStyle(titleStyle);
        }
    }

}
