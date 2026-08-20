package com.bmos.mes.service.dataset.util.options;

import cn.hutool.core.collection.CollectionUtil;
import com.aspose.words.*;
import com.bmos.mes.service.dataset.util.ImageUtil;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

/**
 * docx拍照取证渲染器
 * @author liang
 * @version 1.0.0
 * @date 2024/9/26 11:36
 */
public class DocxEvidencePhotoReplaceOption extends FindReplaceOptions {
    public DocxEvidencePhotoReplaceOption(DocumentBuilder builder, Map<String, byte[]> imageMap, List<DocxTakePhotoLegendReplaceOption.TakePhotoData> evidencePhotoList){
        setLegacyMode(false);
        PageSetup pageSetup = builder.getPageSetup();
        double w = pageSetup.getPageWidth() - pageSetup.getLeftMargin() - pageSetup.getRightMargin();
        double h = pageSetup.getPageHeight() - pageSetup.getTopMargin() - pageSetup.getBottomMargin();
        setReplacingCallback(replacingArgs -> {
            builder.moveTo(replacingArgs.getMatchNode());
            if (CollectionUtil.isNotEmpty(evidencePhotoList)){
                Paragraph paragraph = null;
                for (int i = 0; i < evidencePhotoList.size(); i++) {
                    DocxTakePhotoLegendReplaceOption.TakePhotoData d = evidencePhotoList.get(i);
                    if (i == 0){
                        paragraph = new Paragraph(builder.getDocument());
                        paragraph.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
                        builder.getCurrentParagraph().getParentNode().insertBefore(paragraph, builder.getCurrentParagraph());
                    }
                    builder.moveTo(paragraph);
                    builder.insertBreak(BreakType.PAGE_BREAK);
                    String tf = "取证图片%d(%s %s)";
                    builder.write(String.format(tf, i + 1, d.getCreator(), d.getTime()));
                    // 图片信息
                    byte[] bytes = imageMap.get(d.getImageUrl());
                    if (bytes != null && bytes.length != 0){
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
