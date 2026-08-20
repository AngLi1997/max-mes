package com.bmos.lims2.server.eln.record.util;

import cn.hutool.core.util.StrUtil;
import com.aspose.words.*;
import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import com.bmos.file.docx.model.DocxHeaderFooterItem;
import com.bmos.file.docx.model.DocxOutlineInfo;
import com.bmos.file.docx.util.SplitDocxUtil;
import jodd.util.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/25 14:18
 */
public class DocxSplitUtil2 {


    /**
     * 页码占位符
     */
    private static final Pattern PAGE_PLACEHOLDER = Pattern.compile("<div style=\"-aw-sdt-tag:''\">(.*?)</div>", Pattern.DOTALL);

    /**
     * 空行占位符
     */
    private static final String F = "\f";

    /**
     * 页码占位符
     */
    private static final String PAGE_NUMBER_HOLDER = "{@pageNumber}";


    private static final Logger log = LoggerFactory.getLogger(DocxSplitUtil2.class);

    private static final int nThreads = 10;

    private static final ExecutorService executorService = new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());

    /**
     * 解析word文件 转换为节列表
     * @param file word文件
     * @return 节 列表
     */
    public static List<DocxOutlineInfo> splitDocx(File file) {

        Map<Integer, DocxOutlineInfo> map = new Hashtable<>();

        List<DocxOutlineInfo> resultList = new ArrayList<>();

        Map<Integer, HeaderFooter> lastHeaderFooterMap = new HashMap<>();

        Document document = SplitDocxUtil.loadDocx(file);
        List<CompletableFuture<Void>> tasks = new ArrayList<>();

        // 记录项计数
        AtomicInteger count = new AtomicInteger(0);

        for (int i = 0; i < document.getSections().getCount(); i++) {
            Section section = document.getSections().get(i);
            if (section.getBody().getText().equals(F)){
                continue;
            }
            String title = null;
            for (Paragraph para : (Iterable<Paragraph>) section.getChildNodes(NodeType.PARAGRAPH, true)) {
                // 获取段落格式
                ParagraphFormat format = para.getParagraphFormat();
                // 检查是否为一级大纲
                if (format.getStyleIdentifier() == StyleIdentifier.HEADING_1 ||
                        format.getOutlineLevel() == OutlineLevel.LEVEL_1) {
                    // 输出一级大纲内容
                    if (StrUtil.isBlank(title)){
                        title = para.getText().replaceAll("\r", "").trim();
                    }
                }
            }
            if (StringUtil.isBlank(title)){
                title = "记录项_" + count.incrementAndGet();
            }
            // 处理页眉页脚
            HeaderFooterCollection headersFooters = section.getHeadersFooters();
            // 每节最多有6个页眉页脚 首页头 首页脚 奇数页头 奇数页脚 偶数页头 偶数页脚
            HeaderFooter[] hfs = new HeaderFooter[6];
            for (int hft = 0; hft < HeaderFooterType.getValues().length; hft++) {
                HeaderFooter hf = headersFooters.getByHeaderFooterType(hft);
                if (hf != null){
                    lastHeaderFooterMap.put(hft, hf);
                }
                hfs[hft] = lastHeaderFooterMap.get(hft);
            }
            PageSetup pageSetup = section.getPageSetup();
            // 是否区分奇偶
            boolean oddAndEventDifferent = pageSetup.getOddAndEvenPagesHeaderFooter();
            // 是否区分首页不同
            boolean firstDifferent = pageSetup.getDifferentFirstPageHeaderFooter();
            // 页码起始
            int pageStartingNumber = pageSetup.getPageStartingNumber();
            // 页码样式
            Integer pageNumberStyle = pageSetup.getPageNumberStyle();
            // 页面方向
            int orientation = pageSetup.getOrientation();

            String finalTitle = title;
            int finalI = i;

            // 开启多线程解析
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
                DocxOutlineInfo result = new DocxOutlineInfo();
                result.setMarkName(finalTitle);
                result.setType("content");
                result.setStyle(orientation == Orientation.PORTRAIT);
                try {
                    result.setFileContent(getHtmlContent(section.getRange().toDocument()));
                }catch (Exception ex){
                    log.error("word转html文件解析异常", ex);
                }
                DocxFooter footer = new DocxFooter();
                DocxHeader header = new DocxHeader();
                setHeaderFooter(hfs, header, footer);
                result.setDocxHeader(header);
                result.setDocxFooter(footer);
                result.setFirstDifferent(firstDifferent);
                result.setOddAndEvenDifferent(oddAndEventDifferent);
                result.setPageNumberStyle(pageNumberStyle);
                result.setPageStartingNumber(pageStartingNumber);
                map.put(finalI, result);
            }, executorService);
            tasks.add(future);
        }
        log.info("等待word转html解析结束");
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
        map.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getKey))
                .forEachOrdered(e -> resultList.add(e.getValue()));
        return resultList;
    }

    private static void setHeaderFooter(HeaderFooter[] hfs, DocxHeader header, DocxFooter footer){
        header.setHeaderFirst(getFromHeaderFooter(hfs[HeaderFooterType.HEADER_FIRST]));
        header.setHeaderPrimary(getFromHeaderFooter(hfs[HeaderFooterType.HEADER_PRIMARY]));
        header.setHeaderEven(getFromHeaderFooter(hfs[HeaderFooterType.HEADER_EVEN]));
        footer.setFooterFirst(getFromHeaderFooter(hfs[HeaderFooterType.FOOTER_FIRST]));
        footer.setFooterPrimary(getFromHeaderFooter(hfs[HeaderFooterType.FOOTER_PRIMARY]));
        footer.setFooterEven(getFromHeaderFooter(hfs[HeaderFooterType.FOOTER_EVEN]));
    }

    /**
     * 获取页眉页脚中的内容
     * @param headerFooter
     * @return
     */
    private static DocxHeaderFooterItem getFromHeaderFooter(HeaderFooter headerFooter){
        if (headerFooter != null){
            Integer alignment = getAlignment(headerFooter);
            String content;
            try {
                content = getHtmlContent(headerFooter, alignment);
            }catch (Exception e){
                content = null;
            }
            DocxHeaderFooterItem item = new DocxHeaderFooterItem();
            item.setContent(content);
            item.setPageCodeHorizontalAlignment(alignment);
            return item;
        }
        return null;
    }

    /**
     * 获取页码的对齐位置
     * @param hf
     * @return
     */
    private static @NotNull Integer getAlignment(HeaderFooter hf) {
        Integer pageCodeHorizontalAlignment = null;
        boolean find = false;
        // 找到页码的位置 并进行占位
        for (Object node : hf.getChildNodes(NodeType.PARAGRAPH, true)) {
            Paragraph para = (Paragraph) node;
            String text = para.getText().trim();
            if (text.contains("PAGE")) {
                find = true;
                pageCodeHorizontalAlignment = para.getFrameFormat().getHorizontalAlignment();
                boolean isPageNumber = false;
                for (Run run : para.getRuns()) {
                    if (isPageNumber) {
                        isPageNumber = false;
                    }
                    if (run.getText().contains("PAGE")) {
                        isPageNumber = true;
                    }
                }
                break;
            }
        }
        if (!find) {
            pageCodeHorizontalAlignment = HorizontalAlignment.LEFT;
        }
        return pageCodeHorizontalAlignment;
    }

    private static String getHtmlContent(Document document){
        if (document == null){
            return null;
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            HtmlSaveOptions htmlSaveOptions = new HtmlSaveOptions();
            htmlSaveOptions.setExportImagesAsBase64(true);
            htmlSaveOptions.setCssStyleSheetType(CssStyleSheetType.INLINE);
            htmlSaveOptions.setExportHeadersFootersMode(ExportHeadersFootersMode.NONE);
            document.save(outputStream, htmlSaveOptions);
            return outputStream.toString().replaceAll("&#xa0;", " ");
        }catch (Exception e){
            return null;
        }
    }

    private static String getHtmlContent(HeaderFooter headerFooter, Integer alignment){
        if (headerFooter == null){
            return null;
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Document document = new Document();
            NodeCollection childNodes = headerFooter.getChildNodes(NodeType.ANY, true);
            for (Object childNode : childNodes) {
                Node node = (Node) childNode;
                document.getFirstSection().getBody().appendChild(document.importNode(node, true));
            }
            HtmlSaveOptions htmlSaveOptions = new HtmlSaveOptions();
            htmlSaveOptions.setExportImagesAsBase64(true);
            htmlSaveOptions.setCssStyleSheetType(CssStyleSheetType.INLINE);
            htmlSaveOptions.setPrettyFormat(true);
            htmlSaveOptions.setExportTocPageNumbers(false);
            htmlSaveOptions.setExportPageSetup(false);
            document.save(outputStream, htmlSaveOptions);

            // 空格替换（正常导出html不需要 但是UEditor中会把这个东西显示出来 = =）
            String html = outputStream.toString().replaceAll("&#xa0;", " ");
            String alignmentStr = "left";
            if (alignment == HorizontalAlignment.CENTER){
                alignmentStr = "center";
            }else if (alignment == HorizontalAlignment.RIGHT){
                alignmentStr = "right";
            }
            // 替换页码占位符
            Matcher matcher = PAGE_PLACEHOLDER.matcher(html);
            while (matcher.find()){
                html = html.replaceAll(matcher.group(), getAlinStr(alignmentStr));
            }
            return html;
        }catch (Exception e){
            return null;
        }
    }

    private static String getAlinStr(String alignmentStr){
        return  "<div style=\"-aw-sdt-tag:''\">\n" +
                "      <div style=\"width: 100%; text-align: " + alignmentStr + ";\">" + PAGE_NUMBER_HOLDER + "</div>\n" +
                "    </div>";
    }
}
