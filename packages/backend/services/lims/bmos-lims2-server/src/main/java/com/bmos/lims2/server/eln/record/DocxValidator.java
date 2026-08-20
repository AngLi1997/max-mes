package com.bmos.lims2.server.eln.record;

import com.aspose.words.*;
import com.bmos.file.docx.util.SplitDocxUtil;
import com.bmos.lims2.common.constants.RecordConstant;
import com.bmos.lims2.server.eln.record.dto.RecordFormatResult;
import com.bmos.lims2.server.eln.record.enums.RecordFormatType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.aspose.words.TextOrientation.HORIZONTAL;

/**
 * 批记录格式校验
 * @author liang
 * @version 1.0.0
 * @date 2025/1/6 14:39
 */
public class DocxValidator {

    private static final Logger log = LoggerFactory.getLogger(DocxValidator.class);

    private static final String LOG_PREFIX = "[批记录格式校验]";

    public static List<RecordFormatResult> validate (File file) {
        List<RecordFormatResult> result = new ArrayList<>();
        try {
            Document document = SplitDocxUtil.loadDocx(file);
            DocumentBuilder builder = new DocumentBuilder(document);
            // 超链接 替换为普通文本 添加批注
            result.add(handleHyperlink(builder));
            // 书签 删除 添加批注
            result.add(handleBookmarks(builder));
            // 隐藏文本 不处理 添加批注
            result.add(handleHidden(builder));
            // 分散对齐的文本 修改为左对齐 添加批注
            result.add(handleDistractText(builder));
            // 文字环绕 不处理（暂时无法处理） 添加批注
            result.add(handleTextWrapping(builder));
            // 表格文字方向 不处理（暂时无法处理） 添加批注
            result.add(handleTableTextRotation(builder));
            document.save(file.getAbsolutePath());
        }catch (Exception ex){
            log.error(LOG_PREFIX + "格式校验异常", ex);
        }
        return result;
    }

    /**
     * 清除所有的批注 返回新的文件
     * @param srcFile
     * @throws Exception
     */
    public static File transAndClearComments(File srcFile) throws Exception {

        try {

            Document doc = SplitDocxUtil.loadDocx(srcFile);

            NodeCollection comments = doc.getChildNodes(NodeType.COMMENT, true);

            for (Object commentObj : comments) {
                Comment comment = (Comment) commentObj;
                comment.remove();
            }

            File temp = File.createTempFile(RecordConstant.TEMPORARY_FOLDER, RecordConstant.FILE_TYPE);

            doc.save(temp.getAbsolutePath(), SaveFormat.DOCX);

            return temp;

        } catch (Exception e){

            log.error(LOG_PREFIX + "清除批注异常", e);

            return srcFile;
        }
    }

    /**
     * 超链接 替换为普通文本 添加批注
     */
    private static RecordFormatResult handleHyperlink(DocumentBuilder builder) {
        Document doc = builder.getDocument();
        RecordFormatResult result = new RecordFormatResult(RecordFormatType.HYPER_LINK);
        for (Field field : doc.getRange().getFields()) {
            if (field.getType() == FieldType.FIELD_HYPERLINK) {
                FieldHyperlink hyperlink = (FieldHyperlink) field;
                // 获取超链接的地址
                String hyperlinkDisplayText = field.getResult();
                log.info("{}替换超链接文本:{}", LOG_PREFIX, hyperlinkDisplayText.trim());
                Run newText = new Run(doc, hyperlinkDisplayText);
                Run nextSibling = (Run) hyperlink.getSeparator().getNextSibling();
                newText.getFont().setSize(nextSibling.getFont().getSize());
                hyperlink.getStart().getParentNode().insertBefore(newText, hyperlink.getStart());
                try {
                    hyperlink.remove();
                } catch (Exception e) {
                    log.error(LOG_PREFIX + "超链接引用异常", e);
                }
                result.handle(newText, builder);
            }
        }
        return result;
    }

    /**
     * 处理书签 删除 添加批注
     * @param builder
     */
    private static RecordFormatResult handleBookmarks(DocumentBuilder builder) {
        RecordFormatResult result = new RecordFormatResult(RecordFormatType.BOOKMARKS);
        BookmarkCollection bookmarks = builder.getDocument().getRange().getBookmarks();
        for (Bookmark bookmark : bookmarks) {
            log.info("{}删除书签:{}", LOG_PREFIX, bookmark.getName().trim());
            try {
                result.handle(bookmark.getBookmarkStart(), builder);
                bookmark.remove();
            } catch (Exception e) {
                log.error(LOG_PREFIX + "书签删除异常", e);
            }
        }
        return result;
    }

    /**
     * 处理隐藏文本 不处理 添加批注
     * @param builder
     */
    private static RecordFormatResult handleHidden(DocumentBuilder builder) {
        RecordFormatResult result = new RecordFormatResult(RecordFormatType.HIDDEN_TEXT);
        NodeCollection runs = builder.getDocument().getChildNodes(NodeType.RUN, true);
        for (Object runObj : runs) {
            Run run = (Run) runObj;
            if (run.getFont().getHidden()){
                log.info("{}隐藏文本:{} 不处理", LOG_PREFIX, run.getText().trim());
                result.ignore(run, builder);
            }
        }
        return result;
    }

    /**
     * 处理分散对齐的文本 修改为左对齐 添加批注
     * @param builder
     */
    private static RecordFormatResult handleDistractText(DocumentBuilder builder) {
        RecordFormatResult result = new RecordFormatResult(RecordFormatType.DISTRACT_TEXT);
        // 遍历所有段落
        for (Object paraObj : builder.getDocument().getChildNodes(NodeType.PARAGRAPH, true)) {
            Paragraph para = (Paragraph) paraObj;
            if (para.getParentNode() != null && para.getParentNode().getNodeType() == NodeType.COMMENT) {
                continue;
            }
            // 检查段落对齐方式是否为分散对齐
            if (para.getParagraphFormat().getAlignment() == ParagraphAlignment.JUSTIFY) {
                // 更新为左对齐
                log.info("{}分散对齐文本:{} 修改为左对齐", LOG_PREFIX, para.getText().trim());
                para.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
                result.handle(para.getFirstChild(), builder);
            }
        }
        return result;
    }

    /**
     * 处理文字环绕 不处理（暂时无法处理） 添加批注
     * @param builder
     */
    private static RecordFormatResult handleTextWrapping(DocumentBuilder builder) {
        RecordFormatResult result = new RecordFormatResult(RecordFormatType.TEXT_WRAPPING);
        // 遍历所有的 Table 对象
        for (Table table : (Iterable<Table>) builder.getDocument().getChildNodes(NodeType.TABLE, true)) {
            int textWrapping = table.getTextWrapping();
            if (textWrapping == TextWrapping.AROUND) {
                log.info("{}表格文字环绕 不处理", LOG_PREFIX);
                result.ignore(table, builder);
            }
        }
        return result;
    }

    /**
     * 处理表格文字方向 不处理（暂时无法处理） 添加批注
     * @param builder
     */
    private static RecordFormatResult handleTableTextRotation(DocumentBuilder builder) {
        RecordFormatResult result = new RecordFormatResult(RecordFormatType.TABLE_TEXT_ROTATION);
        // 遍历文档中的表格
        NodeCollection tables = builder.getDocument().getChildNodes(NodeType.TABLE, true);
        for (Table table : (Iterable<Table>) tables) {
            for (Row row : table.getRows()) {
                for (Cell cell : row.getCells()) {
                    // 检查单元格的文本方向
                    int orientation = cell.getCellFormat().getOrientation();
                    if (orientation != HORIZONTAL) {
                        log.info("{}表格文字方向:{} 不处理", LOG_PREFIX, TextOrientation.getName(orientation));
                        result.ignore(cell.getFirstParagraph(), builder);
                    }
                }
            }
        }
        return result;
    }
}
