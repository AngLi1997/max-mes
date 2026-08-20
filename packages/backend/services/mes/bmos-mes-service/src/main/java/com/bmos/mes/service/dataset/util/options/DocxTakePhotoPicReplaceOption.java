package com.bmos.mes.service.dataset.util.options;

import cn.hutool.core.collection.CollectionUtil;
import com.aspose.words.*;
import com.bmos.mes.service.dataset.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * docx渲染器docx渲染器（拍照组件图片）
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/8/21 15:27
 */
public class DocxTakePhotoPicReplaceOption extends FindReplaceOptions {

    public DocxTakePhotoPicReplaceOption(DocumentBuilder builder, Map<String, byte[]> imageMap, LinkedHashMap<String, DocxTakePhotoLegendReplaceOption.TakePhotoData> photoMap) {
        setLegacyMode(false);
        PageSetup pageSetup = builder.getPageSetup();
        double w = pageSetup.getPageWidth() - pageSetup.getLeftMargin() - pageSetup.getRightMargin();
        double h = pageSetup.getPageHeight() - pageSetup.getTopMargin() - pageSetup.getBottomMargin();
        setReplacingCallback(replacingArgs -> {
            builder.moveTo(replacingArgs.getMatchNode());
            if (CollectionUtil.isNotEmpty(photoMap)){
                ArrayList<Map.Entry<String, DocxTakePhotoLegendReplaceOption.TakePhotoData>> list = new ArrayList<>(photoMap.entrySet());
                Paragraph paragraph = null;
                for (int i = 0; i < list.size(); i++) {
                    Map.Entry<String, DocxTakePhotoLegendReplaceOption.TakePhotoData> entry = list.get(i);
                    DocxTakePhotoLegendReplaceOption.TakePhotoData d = entry.getValue();
                    if (i == 0){
                        paragraph = new Paragraph(builder.getDocument());
                        paragraph.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
                        builder.getCurrentParagraph().getParentNode().insertBefore(paragraph, builder.getCurrentParagraph());
                    }
                    builder.moveTo(paragraph);
                    builder.insertBreak(BreakType.PAGE_BREAK);
                    builder.write(entry.getKey());
                    // 图片信息
                    byte[] bytes = imageMap.get(d.getImageUrl());
                    if (bytes != null && bytes.length != 0) {
                        BufferedImage read = ImageIO.read(new ByteArrayInputStream(bytes));
                        Dimension dimension = ImageUtil.getDimensionAuto(read, w, h - DocxRenderConstants.TOP_MARGIN);
                        builder.insertImage(read, dimension.getWidth(), dimension.getHeight());
                    }
                }
            }
            replacingArgs.setReplacement("");
            return ReplaceAction.REPLACE;
        });
    }
}
