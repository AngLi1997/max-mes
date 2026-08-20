package com.bmos.file.docx.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HtmlUtil;
import com.aspose.words.Document;
import com.aspose.words.*;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.file.docx.model.DocxFooter;
import com.bmos.file.docx.model.DocxHeader;
import com.bmos.file.docx.model.DocxOutlineInfo;
import com.bmos.file.docx.model.HeaderFooterTypeSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.Docx4J;
import org.docx4j.XmlUtils;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.model.images.FileConversionImageHandler;
import org.docx4j.model.structure.SectionWrapper;
import org.docx4j.openpackaging.io3.Save;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.org.apache.xml.security.utils.XMLUtils;
import org.docx4j.wml.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STPageOrientation;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author rjg
 * docx文档分割工具类
 */
@Slf4j
public class SplitDocxUtil {

    /**
     * 换行符
     */
    private static final String LINE_BREAK = "\n";
    /**
     * 空格、换页符
     */
    private static final String BLANK = "\\s+";

    private static final String CONTENT = "content";

    private static final String ITEM_DEFAULT_PREFIX = "记录项_";

    public static Document loadDocx(File file) {
//        InputStream is = DocxSplitUtil.class.getClassLoader().getResourceAsStream("license.xml");
//        License license = new License();
//        try {
//            license.setLicense(is);
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
        try {
            LoadOptions loadOptions = new LoadOptions();
            loadOptions.setTempFolder("\\split\\temp\\");
            return new Document(Files.newInputStream(file.toPath()), loadOptions);
        } catch (Exception e) {
            log.error("创建 Document 对象异常：{}", e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.DOCX_CONVERT_ERROR);
        }
    }

    /**
     * 拆分docx文件
     *
     * @param file docx文件
     * @return 拆分后的每个段落名称、html文件集合
     */
    public static List<DocxOutlineInfo> splitDocx(File file) {
        List<DocxOutlineInfo> result = new ArrayList<>();
        Document doc = loadDocx(file);
        AtomicInteger count = new AtomicInteger(0);
        AtomicInteger sectionCount = new AtomicInteger(0);
        // 可链接的上一节页眉页脚
        Map<Integer, HeaderFooter> lastSectionHeaderAndFooter = new HashMap<>();
        // 上一节页码对齐方式
        Map<Integer, Integer> lastSectionPageCodeHorizontalAlignment = new HashMap<>();

        List<CompletableFuture<List<DocxOutlineInfo>>> tasks = new ArrayList<>();
        while (count.get() < doc.getSections().getCount()) {
            int c = count.getAndIncrement();
            Section originSection = doc.getSections().get(c);
            if (Objects.equals(originSection.getBody().getText(), "\f")) {
                continue;
            }
            int i = sectionCount.incrementAndGet();
            HeaderFooterCollection headersFooters = originSection.getHeadersFooters();
            HeaderFooterTypeSetting[] headerFooterTypeSettings = new HeaderFooterTypeSetting[HeaderFooterType.length];
            boolean isFirst = c == 0;
            getEachHeaderFromSection(isFirst, headerFooterTypeSettings, headersFooters, lastSectionHeaderAndFooter, lastSectionPageCodeHorizontalAlignment);
            CompletableFuture<List<DocxOutlineInfo>> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return splitOriginSection(i, originSection, headerFooterTypeSettings);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            tasks.add(future);
        }
        CompletableFuture.allOf(tasks.toArray(new CompletableFuture[0])).join();
        for (CompletableFuture<List<DocxOutlineInfo>> task : tasks) {
            try {
                List<DocxOutlineInfo> docxOutlineInfos = task.get();
                result.addAll(docxOutlineInfos);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    public static HeaderFooterTypeSetting[] getHeaderFromSection(Section section) {
        HeaderFooterTypeSetting[] headerFooterTypeSettings = new HeaderFooterTypeSetting[6];
        getEachHeaderFromSection(true, headerFooterTypeSettings, section.getHeadersFooters(), new HashMap<>(), new HashMap<>());
        return headerFooterTypeSettings;
    }

    private static void getEachHeaderFromSection(boolean isFirst, HeaderFooterTypeSetting[] headerFooterTypeSettings, HeaderFooterCollection headersFooters, Map<Integer, HeaderFooter> lastSectionHeaderAndFooter, Map<Integer, Integer> lastSectionPageCodeHorizontalAlignment) {
        boolean linkToPrevious;

        for (int j = 0; j < HeaderFooterType.length; j++) {
            HeaderFooterTypeSetting setting = headerFooterTypeSettings[j];
            if (setting == null) {
                setting = new HeaderFooterTypeSetting();
                headerFooterTypeSettings[j] = setting;
            }
            HeaderFooter hf = headersFooters.getByHeaderFooterType(j);
            Integer pageCodeHorizontalAlignment = setting.getPageCodeHorizontalAlignment();
            if (hf == null) {
                // 使用链接到的上一节的页眉页脚
                hf = lastSectionHeaderAndFooter.get(j);
                linkToPrevious = false;
                pageCodeHorizontalAlignment = lastSectionPageCodeHorizontalAlignment.getOrDefault(j, HorizontalAlignment.LEFT);
            } else {
                // 未链接到上一节
                linkToPrevious = !isFirst;
                lastSectionHeaderAndFooter.put(j, hf);
                // 查询有无页码 和对齐位置
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
                                run.setText("{@pageNumber}");
                                run.getParentNode().appendChild(run);
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
                lastSectionPageCodeHorizontalAlignment.put(j, pageCodeHorizontalAlignment);
            }
            setting.setHeaderFooter(hf);
            setting.setLinkedToPrevious(linkToPrevious);
            setting.setPageCodeHorizontalAlignment(pageCodeHorizontalAlignment);
        }
    }

    private static XWPFDocument convertXwpfDocument(Node node, Document newDoc) throws Exception {
        ByteArrayOutputStream docOut = new ByteArrayOutputStream();
        if (node instanceof Section) {
            newDoc.getSections().clear();
            Section newSection = (Section) newDoc.importNode(node, true);
            for (HeaderFooter headersFooter : newSection.getHeadersFooters()) {
                newSection.removeChild(headersFooter);
            }
            newDoc.getSections().add(newSection);
        }
        SaveOptions saveOptions = SaveOptions.createSaveOptions(SaveFormat.DOCX);
        newDoc.save(docOut, saveOptions);
        XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(docOut.toByteArray()));
        docOut.close();
        return document;
    }

    /**
     * 解析段落信息
     *
     * @param index         编号(未解析出一级大纲时使用index作为后缀命名记录项名称)
     * @param originSection 段落信息
     * @param settings      页眉页脚设置 里面固定为六个类型 对应0-5页眉页脚类型
     * @return 解析结果
     * @throws Exception
     */
    public static List<DocxOutlineInfo> splitOriginSection(int index, Section originSection, HeaderFooterTypeSetting[] settings) throws Exception {
        List<DocxOutlineInfo> result = new ArrayList<>();
        PageSetup pageSetup = originSection.getPageSetup();
        boolean oddAndEventDifferent = pageSetup.getOddAndEvenPagesHeaderFooter();
        boolean firstDifferent = pageSetup.getDifferentFirstPageHeaderFooter();
        int pageStartingNumber = pageSetup.getPageStartingNumber();
        Integer pageNumberStyle = pageSetup.getPageNumberStyle();
        Section section = originSection.deepClone();
        Document newDoc = new Document();
        XWPFDocument document = convertXwpfDocument(section, newDoc);
        boolean find = false;
        String name = null;
        for (IBodyElement bodyElement : document.getBodyElements()) {
            if (bodyElement instanceof XWPFParagraph && bodyElement.getElementType() == BodyElementType.PARAGRAPH) {
                XWPFParagraph para = (XWPFParagraph) bodyElement;
                if ((ObjectUtil.isNotEmpty(para.getCTP().getPPr()) && para.getCTP().getPPr().getOutlineLvl() != null
                        && para.getCTP().getPPr().getOutlineLvl().getVal().compareTo(BigInteger.ZERO) == 0) ||
                        (ObjectUtil.isNotEmpty(document.getStyles().getStyle(para.getStyleID())) &&
                                document.getStyles().getStyle(para.getStyleID()).getCTStyle().getPPr() != null &&
                                document.getStyles().getStyle(para.getStyleID()).getCTStyle().getPPr().getOutlineLvl() != null &&
                                document.getStyles().getStyle(para.getStyleID()).getCTStyle().getPPr().getOutlineLvl().getVal().compareTo(BigInteger.ZERO) == 0)) {
                    // 解析一级大纲
                    name = para.getParagraphText().replaceAll(BLANK, "").replace(LINE_BREAK, "");
                    if (StrUtil.isNotBlank(name)) {
                        find = true;
                        break;
                    }
                }
            }
        }
        if (!find) {
            // 未解析到一级大纲 设置默认值
            name = ITEM_DEFAULT_PREFIX + index;
        }
        DocxOutlineInfo info = convert(document, name, CONTENT);
        long start = System.currentTimeMillis();
        extractHeaderFooter(settings, oddAndEventDifferent, firstDifferent, pageStartingNumber, pageNumberStyle, info);
        long end = System.currentTimeMillis();
        System.out.println(index + " 耗时:" + (end - start));
        result.add(info);
        return result;
    }

    private static void extractHeaderFooter(HeaderFooterTypeSetting[] settings,
                                            boolean oddAndEventDifferent,
                                            boolean firstDifferent,
                                            int pageStartingNumber,
                                            Integer pageNumberStyle,
                                            DocxOutlineInfo info) throws Exception {
        if (settings == null || settings.length != 6) {
            return;
        }
        for (int i = 0; i < settings.length; i++) {

            // 页眉页脚
            HeaderFooter hf = settings[i].getHeaderFooter();
            // 是否链接到上一页
            Boolean linkedToPrevious = settings[i].getLinkedToPrevious();
            // 页码对齐方式
            Integer pageCodeHorizontalAlignment = settings[i].getPageCodeHorizontalAlignment();

            info.setFirstDifferent(firstDifferent);
            info.setOddAndEvenDifferent(oddAndEventDifferent);
            info.setPageStartingNumber(pageStartingNumber);
            info.setPageNumberStyle(pageNumberStyle);

            if (hf == null) {
                continue;
            }

            // 是否为header
            boolean isHeader = hf.isHeader();
            // 保存页眉页脚转化的word文件 byteArray
            ByteArrayOutputStream docOutput = new ByteArrayOutputStream();
            Document document = hf.getRange().toDocument();
            document.save(docOutput, SaveFormat.DOCX);
            // 解析文档（用于渲染html）
            WordprocessingMLPackage wordprocessingMlPackage = Docx4J.load(new ByteArrayInputStream(docOutput.toByteArray()));
            SectionWrapper sectionWrapper = wordprocessingMlPackage.getDocumentModel().getSections().get(0);
            SectPr sectPr = sectionWrapper.getSectPr();
            // 转html或自动读取default类型的页眉页脚，所以需要将first\even对应的页眉页脚转换为default类型
            List<CTRel> egHdrFtrReferences = sectPr.getEGHdrFtrReferences();
            for (CTRel egHdrFtrReference : egHdrFtrReferences) {
                if (egHdrFtrReference instanceof HeaderReference) {
                    HeaderReference headerReference = (HeaderReference) egHdrFtrReference;
                    headerReference.setType(HdrFtrRef.DEFAULT);
                } else if (egHdrFtrReference instanceof FooterReference) {
                    FooterReference footerReference = (FooterReference) egHdrFtrReference;
                    footerReference.setType(HdrFtrRef.DEFAULT);
                }
            }
            HTMLSettings htmlSettings = new HTMLSettings();
            htmlSettings.setOpcPackage(wordprocessingMlPackage);
            // 图片处理为base64
            htmlSettings.setImageHandler(new FileConversionImageHandler("", true));
            // 保存渲染html的byteArray
            ByteArrayOutputStream htmlByteArray = new ByteArrayOutputStream();
            Docx4J.toHTML(htmlSettings, htmlByteArray, Docx4J.FLAG_NONE);
            // 整个文档的html
            String wordHtml = new String(htmlByteArray.toByteArray(), StandardCharsets.UTF_8);
            // 最终页眉/页脚的html
            String cleanXml = extractedCleanXml(wordHtml, isHeader);
            if (isHeader) {
                DocxHeader docxHeader = info.getDocxHeader();
                if (docxHeader == null) {
                    info.setDocxHeader(new DocxHeader());
                }
                info.getDocxHeader().fillHeader(cleanXml, pageCodeHorizontalAlignment, i);
                info.getDocxHeader().setLinkToPrevious(linkedToPrevious);
            } else {
                DocxFooter docxFooter = info.getDocxFooter();
                if (docxFooter == null) {
                    info.setDocxFooter(new DocxFooter());
                }
                info.getDocxFooter().fillFooter(cleanXml, pageCodeHorizontalAlignment, i);
                info.getDocxFooter().setLinkToPrevious(linkedToPrevious);
            }
        }
    }


    private static DocxOutlineInfo convert(XWPFDocument document, String name, String type) {
        try (ByteArrayOutputStream finalPartOut = new ByteArrayOutputStream(); ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            document.write(finalPartOut);
            WordprocessingMLPackage wordprocessingMlPackage = Docx4J.load(new ByteArrayInputStream(finalPartOut.toByteArray()));
            HTMLSettings htmlSettings = new HTMLSettings();
            htmlSettings.setOpcPackage(wordprocessingMlPackage);
            // 图片作为base64处理
            htmlSettings.setImageHandler(new FileConversionImageHandler("", true));
            Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);
            String content = new String(os.toByteArray(), StandardCharsets.UTF_8);
            DocxOutlineInfo info = new DocxOutlineInfo();
            STPageOrientation.Enum orient = document.getDocument().getBody().getSectPr().getPgSz().getOrient();
            if (ObjectUtil.isNotEmpty(orient) && "LANDSCAPE".equalsIgnoreCase(orient.toString())) {
                info.setStyle(Boolean.FALSE);
            } else {
                info.setStyle(Boolean.TRUE);
            }
            info.setMarkName(name);
            info.setFileContent(content);
            info.setType(type);
            info.setSrcDoc(document);
            return info;

        } catch (Exception e) {
            log.error("DOCX转换异常：{}", e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.DOCX_CONVERT_ERROR);
        }
    }

    public static XWPFDocument convertDocument(File file) {
        InputStream is = DocxSplitUtil.class.getClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        try {
            license.setLicense(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        XWPFDocument document;
        try {
            LoadOptions loadOptions = new LoadOptions();
            loadOptions.setTempFolder("\\split\\temp\\");
            Document doc = new Document(Files.newInputStream(file.toPath()), loadOptions);
            Section section = doc.getSections().get(0).deepClone();
            Document newDoc = new Document();
            document = convertXwpfDocument(section, newDoc);
        } catch (Exception e) {
            log.error("创建 Document 对象异常：{}", e.getCause() + e.getMessage());
            throw new BmosException(BaseResponseCode.DOCX_CONVERT_ERROR);
        }
        return document;
    }

    /**
     * 提取页眉/页脚的html
     *
     * @param wordHtml word文档的html
     * @param isHeader 是否为页眉
     * @return 页眉/页脚的html
     */

    private static String extractedCleanXml(String wordHtml, boolean isHeader) {
        return Optional.ofNullable(wordHtml)
                .map(item -> {
                    org.jsoup.nodes.Document doc = Jsoup.parse(item);
                    doc.outputSettings().prettyPrint(false);
                    return doc;
                })
                .map(item -> {
                    item.getElementsByClass("document").remove();
                    if (isHeader) {
                        item.getElementsByClass("footer").remove();
                    } else {
                        item.getElementsByClass("header").remove();
                    }
                    // todo liang 之前手动添加的 white-space会有连续换行的问题 先不添加 但是不添加又会有合并空格的问题
                    // 哈哈哈哈哈哈哈哈哈哈 精彩精彩
//                    for (Element element : item.getElementsByClass(isHeader ? "header" : "footer")) {
//                        if (element.wholeOwnText().contains(" ")) {
//                            element.attr("style", element.attr("style") + " white-space:pre-wrap;");
//                        }
//                    }
                    return item;
                })
                .map(org.jsoup.nodes.Node::toString)
                .orElse(null);
    }


    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<DocxOutlineInfo> docxOutlineInfos = splitDocx(new File("D:\\test\\JSB001乳膏批生产记录（上传版） .docx"));
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
