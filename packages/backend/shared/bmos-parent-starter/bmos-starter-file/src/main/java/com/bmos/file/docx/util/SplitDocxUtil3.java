package com.bmos.file.docx.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BaseResponseCode;
import com.bmos.common.exception.BmosException;
import com.bmos.file.docx.model.DocxOutlineInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.*;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.fonts.IdentityPlusMapper;
import org.docx4j.fonts.Mapper;
import org.docx4j.fonts.PhysicalFont;
import org.docx4j.fonts.PhysicalFonts;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.org.apache.poi.util.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author rjg
 * docx文档分割工具类
 */
@Slf4j
public class SplitDocxUtil3 {

    /**
     * 换行符
     */
    private static final String LINE_BREAK = "\n";
    /**
     * 空格、换页符
     */
    private static final String BLANK = "\\s+";

    private static final String FOOTER_NAME = "页脚";

    private static final String HEADER_NAME = "页眉";

    public static void main(String[] args) {
        String path = "D:\\文档\\人血白蛋白批生产记录.docx";
        long start = System.currentTimeMillis();
        splitDocx(new File(path));
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end -start));
    }


    /**
     * 拆分docx文件
     *
     * @param file docx文件
     * @return 拆分后的每个段落名称、html文件集合
     */
    public static List<DocxOutlineInfo> splitDocx(File file) {
        XWPFDocument doc;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] bytes = IOUtils.toByteArray(fileInputStream);
            doc = new XWPFDocument(new ByteArrayInputStream(bytes));
            //段落
            List<Integer> duanLuo = new ArrayList<>();
            // 大纲名称
            List<String> outLineName = new ArrayList<>();
            //遍历添加大纲名称以及段落
            for (int i = 0; i < doc.getBodyElements().size(); i++) {
                IBodyElement bodyElement = doc.getBodyElements().get(i);
                if (bodyElement instanceof XWPFParagraph) {
                    XWPFParagraph para = (XWPFParagraph) bodyElement;
                    //添加段落的下标
                    if (ObjectUtil.isNotEmpty(para.getCTP().getPPr()) && para.getCTP().getPPr().getOutlineLvl() != null) {
                        duanLuo.add(i);
                    }
                    //添加段落名称
                    if (bodyElement.getElementType() == BodyElementType.PARAGRAPH) {
                        if (ObjectUtil.isNotEmpty(para.getCTP().getPPr()) && para.getCTP().getPPr().getOutlineLvl() != null) {
                            outLineName.add(para.getParagraphText().replaceAll(BLANK, "").replace(LINE_BREAK, ""));
                        }
                    }
                }
            }
            if (CollUtil.isEmpty(duanLuo)) {
                return Collections.emptyList();
            }
            List<DocxOutlineInfo> list = footerAndHeaderHandle(doc, outputStream, os);
            os.reset();
            outputStream.reset();
            int max = doc.getBodyElements().size() - 1;
            duanLuo.add(max);
            //遍历按照段落下标对文档进行处理移除
            for (int k = 0; k < duanLuo.size() - 1; k++) {
                doc = new XWPFDocument(new FileInputStream(file));
                //移除文档之后内容
                for (int u = max; u > duanLuo.get(k + 1) - 1; u--) {
                    doc.removeBodyElement(u);
                }
                //移除文档之前内容
                for (int l = duanLuo.get(k) - 1; l >= 0; l--) {
                    doc.removeBodyElement(l);
                }
                docHandle(doc);
                doc.write(outputStream);

                WordprocessingMLPackage wordprocessingMlPackage = Docx4J.load(new ByteArrayInputStream(outputStream.toByteArray()));
                setFontMapper(wordprocessingMlPackage);
                HTMLSettings htmlSettings = new HTMLSettings();
                htmlSettings.setOpcPackage(wordprocessingMlPackage);
                Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);

                DocxOutlineInfo info = new DocxOutlineInfo();
                info.setMarkName(outLineName.get(k));
                info.setFileContent(new String(os.toByteArray(), StandardCharsets.UTF_8));
                //info.setType(Boolean.FALSE);
                list.add(info);
                os.reset();
                outputStream.reset();
            }
            return null;
        } catch (Exception e) {
            log.error("docx解析报错，错误信息：{}", e.getMessage());
            throw new BmosException(BaseResponseCode.TRY_AGAIN_LATER);
        }
    }


    public static <T extends Serializable> T deepCopy(T object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);
            oos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            @SuppressWarnings("unchecked")
            T copy = (T) ois.readObject();
            ois.close();

            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 处理拆分后文件的页眉与页尾
     *
     * @param doc
     */
    public static void docHandle(XWPFDocument doc) {
        //获取页尾集合
        List<XWPFFooter> footerList = doc.getFooterList();
        for (XWPFFooter f : footerList) {
            ArrayList<XWPFParagraph> freshFooter = new ArrayList<>(f.getParagraphs());
            freshFooter.forEach(f::removeParagraph);
            ArrayList<XWPFTable> tabFooter = new ArrayList<>(f.getTables());
            tabFooter.forEach(f::removeTable);
        }
        //删除页头
        List<XWPFHeader> pageTop = doc.getHeaderList();
        for (XWPFHeader top : pageTop) {
            List<XWPFParagraph> freshPageTop = new ArrayList<>(top.getParagraphs());
            freshPageTop.forEach(top::removeParagraph);
            List<XWPFTable> tabPageTop = new ArrayList<>(top.getTables());
            tabPageTop.forEach(top::removeTable);
        }
    }

    //处理页眉页脚
    public static List<DocxOutlineInfo> footerAndHeaderHandle(XWPFDocument doc, ByteArrayOutputStream outputStream, ByteArrayOutputStream os) {
        try {
            List<DocxOutlineInfo> parseInfo = new ArrayList<>();
            List<XWPFFooter> footerList = doc.getFooterList();
            List<XWPFHeader> headerList = doc.getHeaderList();
            doc.write(outputStream);
            if (CollUtil.isNotEmpty(footerList) || CollUtil.isNotEmpty(headerList)) {
                WordprocessingMLPackage wordprocessingMlPackage = Docx4J.load(new ByteArrayInputStream(outputStream.toByteArray()));
                HTMLSettings htmlSettings = new HTMLSettings();
                htmlSettings.setOpcPackage(wordprocessingMlPackage);
                Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);
            }
            String docString = new String(os.toByteArray(), StandardCharsets.UTF_8);
            DocxOutlineInfo header = new DocxOutlineInfo();
            header.setMarkName(HEADER_NAME);
            String headerContent = StrUtil.subBefore(docString, "</div>", false) + "</div></body></html>";
            header.setFileContent(headerContent);
            //header.setType(Boolean.TRUE);
            parseInfo.add(header);
            DocxOutlineInfo footerInfo = new DocxOutlineInfo();
            String footer = StrUtil.subBefore(docString, "<body>", false);
            String footerContent = footer + "<body>" + "<div " + StrUtil.subAfter(docString, "<div", true);
            footerInfo.setMarkName(FOOTER_NAME);
            footerInfo.setFileContent(footerContent);
            //footerInfo.setType(Boolean.TRUE);
            parseInfo.add(footerInfo);
            return parseInfo;
        } catch (Exception e) {
            throw new BmosException(BaseResponseCode.TRY_AGAIN_LATER);
        }
    }

    private static void setFontMapper(WordprocessingMLPackage mlPackage) throws Exception {
        Mapper fontMapper = new IdentityPlusMapper();
        //加载字体文件（解决linux环境下无中文字体问题）
        //PhysicalFonts.addPhysicalFonts("SimSun", new URL("simsun.ttc"));
        fontMapper.put("隶书", PhysicalFonts.get("LiSu"));
        fontMapper.put("宋体", PhysicalFonts.get("SimSun"));
        fontMapper.put("微软雅黑", PhysicalFonts.get("Microsoft Yahei"));
        fontMapper.put("黑体", PhysicalFonts.get("SimHei"));
        fontMapper.put("楷体", PhysicalFonts.get("KaiTi"));
        fontMapper.put("新宋体", PhysicalFonts.get("NSimSun"));
        fontMapper.put("华文行楷", PhysicalFonts.get("STXingkai"));
        fontMapper.put("华文仿宋", PhysicalFonts.get("STFangsong"));
        fontMapper.put("仿宋", PhysicalFonts.get("FangSong"));
        fontMapper.put("幼圆", PhysicalFonts.get("YouYuan"));
        fontMapper.put("华文宋体", PhysicalFonts.get("STSong"));
        fontMapper.put("华文中宋", PhysicalFonts.get("STZhongsong"));
        fontMapper.put("等线", PhysicalFonts.get("SimSun"));
        fontMapper.put("等线 Light", PhysicalFonts.get("SimSun"));
        fontMapper.put("华文琥珀", PhysicalFonts.get("STHupo"));
        fontMapper.put("华文隶书", PhysicalFonts.get("STLiti"));
        fontMapper.put("华文新魏", PhysicalFonts.get("STXinwei"));
        fontMapper.put("华文彩云", PhysicalFonts.get("STCaiyun"));
        fontMapper.put("方正姚体", PhysicalFonts.get("FZYaoti"));
        fontMapper.put("方正舒体", PhysicalFonts.get("FZShuTi"));
        fontMapper.put("华文细黑", PhysicalFonts.get("STXihei"));
        fontMapper.put("宋体扩展", PhysicalFonts.get("simsun-extB"));
        fontMapper.put("仿宋_GB2312", PhysicalFonts.get("FangSong_GB2312"));
        fontMapper.put("新細明體", PhysicalFonts.get("SimSun"));
        //解决宋体（正文）和宋体（标题）的乱码问题
        PhysicalFonts.put("PMingLiU", PhysicalFonts.get("SimSun"));
        PhysicalFonts.put("新細明體", PhysicalFonts.get("SimSun"));
        //宋体&新宋体
        PhysicalFont simsunFont = PhysicalFonts.get("SimSun");
        fontMapper.put("SimSun", simsunFont);
        //设置字体
        mlPackage.setFontMapper(fontMapper);
    }
}
