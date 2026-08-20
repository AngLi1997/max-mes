package com.bmos.file.docx.util;

import cn.hutool.core.io.FileUtil;
import com.aspose.words.*;
import com.aspose.words.Shape;
import org.docx4j.Docx4J;
import org.docx4j.convert.out.HTMLSettings;
import org.docx4j.model.images.FileConversionImageHandler;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/6/20 11:30
 */
public class ShapeUtil {
    public static void main(String[] args) throws Exception {
        String filePath = "/Users/liang/Downloads/shape.docx";
        InputStream is = DocxSplitUtil.class.getClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);
        LoadOptions loadOptions = new LoadOptions();
        loadOptions.setTempFolder("\\split\\temp\\");
        Document doc = new Document(Files.newInputStream(new File(filePath).toPath()), loadOptions);
        Body body = doc.getFirstSection().getBody();
        Node node = body.getChildNodes(NodeType.PARAGRAPH, true).get(1);
        Paragraph paragraph = (Paragraph) node;
        Shape shape = (Shape) paragraph.getChildNodes(NodeType.SHAPE, true).get(0);
        shape.setFillColor(Color.WHITE);
        // 操作shape

        ShapeRenderer shapeRenderer = new ShapeRenderer(shape);
        ImageSaveOptions imageSaveOptions = new ImageSaveOptions(SaveFormat.PNG);
        ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
        shapeRenderer.save(imageOut, imageSaveOptions);

        // 将图片插入到文档
        DocumentBuilder documentBuilder = new DocumentBuilder(doc);
        // 设置光标
        documentBuilder.moveTo(shape);
        documentBuilder.insertImage(imageOut.toByteArray());

        // 操作shape结束
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        doc.save(out, SaveFormat.DOCX);
        doc.save("/Users/liang/Downloads/index2.docx", SaveFormat.DOCX);

        WordprocessingMLPackage wordprocessingMlPackage = Docx4J.load(new ByteArrayInputStream(out.toByteArray()));
        HTMLSettings htmlSettings = new HTMLSettings();
        htmlSettings.setOpcPackage(wordprocessingMlPackage);
        htmlSettings.setImageHandler(new FileConversionImageHandler("", true));
        // 保存渲染html的byteArray
        ByteArrayOutputStream htmlByteArray = new ByteArrayOutputStream();
        Docx4J.toHTML(htmlSettings, htmlByteArray, Docx4J.FLAG_NONE);
        FileUtil.writeString(htmlByteArray.toString(), "/Users/liang/Downloads/index.html", "UTF-8");
    }
}
