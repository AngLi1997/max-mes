package com.bmos.file.docx.util;


import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.ObjectUtil;
import com.aspose.words.*;
import com.bmos.file.docx.model.DocxOutlineInfo;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;


public class DocxSplitUtil {

//
//    /**
//     * 换行符
//     */
//    private static final String LINE_BREAK = "\n";
//    /**
//     * 空格、换页符
//     */
//    private static final String BLANK = "\\s+";
//
//    public static void main(String[] args) throws Exception {
//
//        InputStream is = DocxSplitUtil.class.getClassLoader().getResourceAsStream("license.xml");
//        License license = new License();
//        license.setLicense(is);
//        String filePath = "C:\\Users\\Administrator\\Desktop\\QMS.PR7.3-CI-SOP001-R01-00  胶原蛋白植入剂生产记录（已处理）.docx";
//        Document doc = new Document(filePath);
//        HeaderFooterCollection headersFooters = doc.getFirstSection().getHeadersFooters();
//
////        headersFooters.forEach();
//
//    }
//
//    private static void execute(String t) throws Exception {
//
//
//        InputStream is = DocxSplitUtil.class.getClassLoader().getResourceAsStream("license.xml");
//        License license = new License();
//        license.setLicense(is);
//        String filePath = "C:\\Users\\Administrator\\Desktop\\QMS.PR7.3-CI-SOP001-R01-00  胶原蛋白植入剂生产记录（已处理）.docx";
//        Document doc = new Document(filePath);
//
//
//        final int total = doc.getSections().getCount() - 1;
//        AtomicInteger count = new AtomicInteger(total);
//        ExecutorService executorService = ThreadUtil.newExecutor(3, 3, 1000);
//
//        ArrayList<CompletableFuture<Void>> futures = new ArrayList<>();
//
//        while (count.get() > 0) {
//            Section originSection = doc.getSections().get(count.getAndDecrement());
//
//            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
//                try {
//                    doExecute(t, originSection);
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//
//            }, executorService);
//            futures.add(future);
//        }
//
//        // 等待所有任务完成
//        CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
//
//        try {
//            allOf.get();
//            // 阻塞直到所有任务完成
//        } catch (InterruptedException | ExecutionException e) {
//            e.printStackTrace();
//        } finally {
//            // 关闭线程池
//            executorService.shutdownNow();
//        }
//
////            }
//
////        }
//
////        doc.save("D:\\test\\test"+t+".html");
//    }
//
//    private static List<DocxOutlineInfo> doExecute(String t, Section originSection) throws Exception {
//        if (originSection.getBody().getText().trim().isEmpty()) {
//            return Collections.emptyList();
//        }
////            // Split a document into smaller parts, in this instance, split by section.
//        Section section = originSection.deepClone();
//        Document newDoc = new Document();
//        newDoc.getSections().clear();
//        Section newSection = (Section) newDoc.importNode(section, true);
//        newDoc.getSections().add(newSection);
//        // Save each section as a separate document.
//
//        ByteArrayOutputStream docOut = new ByteArrayOutputStream();
//        SaveOptions saveOptions = SaveOptions.createSaveOptions(SaveFormat.DOCX);
//        newDoc.save(docOut, saveOptions);
//        XWPFDocument document = new XWPFDocument(new ByteArrayInputStream(docOut.toByteArray()));
//        for (IBodyElement bodyElement : document.getBodyElements()) {
//            if (bodyElement instanceof XWPFParagraph && bodyElement.getElementType() == BodyElementType.PARAGRAPH) {
//                XWPFParagraph para = (XWPFParagraph) bodyElement;
//                if (ObjectUtil.isNotEmpty(para.getCTP().getPPr()) && para.getCTP().getPPr().getOutlineLvl() != null) {
//                    task(t, document, para);
//                }
//            }
//        }
//
//        docOut.close();
//    }
//
//    private static void task(String t, XWPFDocument document, XWPFParagraph para) throws IOException, Docx4JException {
//        String name = para.getParagraphText().replaceAll(BLANK, "").replace(LINE_BREAK, "");
//        ByteArrayOutputStream finalPartOut = new ByteArrayOutputStream();
//        document.write(finalPartOut);
//        WordprocessingMLPackage wordprocessingMlPackage = Docx4J.load(new ByteArrayInputStream(finalPartOut.toByteArray()));
//        FileOutputStream os = new FileOutputStream("D:\\test\\" + name + t + ".html");
//        HTMLSettings htmlSettings = new HTMLSettings();
//        htmlSettings.setOpcPackage(wordprocessingMlPackage);
//        Docx4J.toHTML(htmlSettings, os, Docx4J.FLAG_NONE);
//
//        finalPartOut.close();
//        os.close();
//    }
}