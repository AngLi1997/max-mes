package com.bmos.mes.service.dataset.util.options;

import com.aspose.words.*;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.service.dataset.common.DatasetTransValueData;
import com.bmos.mes.service.dataset.util.ImageUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.Map;

/**
 * docx渲染器（含说明文字的图片）
 */
public class DocxImageCaptionReplaceOption extends FindReplaceOptions {

    Logger log = LoggerFactory.getLogger(DocxImageCaptionReplaceOption.class);

    public DocxImageCaptionReplaceOption(DocumentBuilder builder, DatasetTransValueData data,
                                         Map<String, byte[]> imageMap) {
        setLegacyMode(false);
        PageSetup pageSetup = builder.getPageSetup();
        double w = pageSetup.getPageWidth() - pageSetup.getLeftMargin() - pageSetup.getRightMargin();
        double h = pageSetup.getPageHeight() - pageSetup.getTopMargin() - pageSetup.getBottomMargin();
        setReplacingCallback(replacingArgs -> {
            builder.moveTo(replacingArgs.getMatchNode());
            String replacement = replacingArgs.getReplacement();
            ImageCaptionData imageInfo = JsonUtils.parseObject(replacement, ImageCaptionData.class);
            String url = imageInfo.getImageUrl();
            byte[] bytes = imageMap.get(url);
            builder.moveTo(replacingArgs.getMatchNode());
            builder.insertBreak(BreakType.PARAGRAPH_BREAK);
            builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
            builder.writeln(imageInfo.getImageCaption());
            try {
                BufferedImage read = ImageIO.read(new ByteArrayInputStream(bytes));
                Dimension dimension = ImageUtil.getDimensionAuto(read, w, h - DocxRenderConstants.TOP_MARGIN);
                builder.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
                builder.insertImage(read, dimension.getWidth(), dimension.getHeight());
            }catch (Exception e){
                log.error("图片渲染失败", e);
            }
            replacingArgs.setReplacement("");
            return ReplaceAction.REPLACE;
        });
    }

    @Data
    public static class ImageCaptionData {

        /**
         * 图片url
         */
        private String imageUrl;

        /**
         * 图片说明文字
         */
        private String imageCaption;
    }
}
