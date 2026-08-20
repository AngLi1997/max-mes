package com.bmos.mes.service.dataset.util.options;

import cn.hutool.core.collection.CollectionUtil;
import com.aspose.words.*;
import com.bmos.mes.service.dataset.util.DocxRenderUtil;
import com.bmos.mes.service.dataset.util.ImageUtil;
import org.apache.commons.lang3.StringUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * docx渲染器docx渲染器(正则表达式条件匹配)
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/2/7 13:27
 */
public class DocxPatternTakePhotoPicReplaceOption extends FindReplaceOptions {
    public DocxPatternTakePhotoPicReplaceOption(DocumentBuilder builder, Map<String, byte[]> imageMap, LinkedHashMap<String, DocxTakePhotoLegendReplaceOption.TakePhotoData> photoMap, String pattern) {
        setLegacyMode(false);
        PageSetup pageSetup = builder.getPageSetup();
        double w = pageSetup.getPageWidth() - pageSetup.getLeftMargin() - pageSetup.getRightMargin();
        double h = pageSetup.getPageHeight() - pageSetup.getTopMargin() - pageSetup.getBottomMargin();
        setReplacingCallback(replacingArgs -> {
            Matcher matcher = Pattern.compile(pattern).matcher(replacingArgs.getMatchNode().getParentNode().getRange().getText());
            String[] index = new String[5];
            if (matcher.find()) {
                String text = matcher.group();
                index = DocxRenderUtil.extractImagePlaceHolderWithPattern(text);
            }
            if (!StringUtils.equals(index[2], "0")){
                index[2] = String.valueOf(Integer.parseInt(index[2]) - 1);
            }
            Map<String, Optional<DocxTakePhotoLegendReplaceOption.TakePhotoData>> maxBatchIndex = photoMap.values().stream().collect(Collectors.groupingBy(
                    DocxTakePhotoLegendReplaceOption.TakePhotoData::getProcessName,
                    Collectors.maxBy(
                            Comparator.comparingInt(item -> Integer.parseInt(item.getBatchIndex()))
                    )
            ));
            builder.moveTo(replacingArgs.getMatchNode());
            if (CollectionUtil.isNotEmpty(photoMap)){
                ArrayList<Map.Entry<String, DocxTakePhotoLegendReplaceOption.TakePhotoData>> list = new ArrayList<>(photoMap.entrySet());
                String[] finalIndex = index;
                // 根据占位符中的工艺/工序等进行过滤
                if (index[2].isEmpty()){
                    index[2] = String.valueOf(maxBatchIndex.get(index[0]).get().getBatchIndex());
                }
                List<Map.Entry<String, DocxTakePhotoLegendReplaceOption.TakePhotoData>> finalList = list.stream()
                        .filter(i -> StringUtils.equals(i.getValue().getProcessName(), finalIndex[0]))
                        .filter(i -> StringUtils.equals(i.getValue().getProcedureName(), finalIndex[1]))
                        .filter(i -> StringUtils.equals(i.getValue().getBatchIndex(), finalIndex[2]))
                        .filter(i -> StringUtils.equals(String.valueOf(i.getValue().getProcessChangeNumber()), finalIndex[3]))
                        .filter(i -> StringUtils.equals(String.valueOf(i.getValue().getProcedureChangeNumber()), finalIndex[4]))
                        .collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(finalList)){
                    Paragraph paragraph = null;
                    for (int i = 0; i < finalList.size(); i++) {
                        Map.Entry<String, DocxTakePhotoLegendReplaceOption.TakePhotoData> entry = finalList.get(i);
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
//                        if (i != finalList.size() - 1){
//                            builder.insertBreak(BreakType.PAGE_BREAK);
//                        }
                    }
                }
            }
            replacingArgs.setReplacement("");
            return ReplaceAction.REPLACE;
        });
    }
}
