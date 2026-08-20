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
 * docx拍照取证渲染器(正则表达式条件匹配)
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/2/7 13:27
 */
public class DocxPatternEvidencePhotoReplaceOption extends FindReplaceOptions {
    public DocxPatternEvidencePhotoReplaceOption(DocumentBuilder builder, Map<String, byte[]> imageMap, List<DocxTakePhotoLegendReplaceOption.TakePhotoData> evidencePhotoList, String pattern) {
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

            Map<String, Optional<DocxTakePhotoLegendReplaceOption.TakePhotoData>> maxBatchIndex = evidencePhotoList.stream().collect(Collectors.groupingBy(
                    DocxTakePhotoLegendReplaceOption.TakePhotoData::getProcessName,
                    Collectors.maxBy(
                            Comparator.comparingInt(item -> Integer.parseInt(item.getBatchIndex()))
                    )
            ));

            Map<Integer, DocxTakePhotoLegendReplaceOption.TakePhotoData> indexMap = new HashMap<>();
            for (int i = 0; i < evidencePhotoList.size(); i++) {
                DocxTakePhotoLegendReplaceOption.TakePhotoData takePhotoData = evidencePhotoList.get(i);
                if (index[2].isEmpty()){
                    index[2] = String.valueOf(maxBatchIndex.get(index[0]).get().getBatchIndex());
                }
                if (StringUtils.equals(takePhotoData.getProcessName(), index[0])
                        && StringUtils.equals(takePhotoData.getProcedureName(), index[1])
                        && StringUtils.equals(takePhotoData.getBatchIndex(), index[2])
                        && StringUtils.equals(String.valueOf(takePhotoData.getProcessChangeNumber()), index[3])
                        && StringUtils.equals(String.valueOf(takePhotoData.getProcedureChangeNumber()), index[4])) {
                    indexMap.put(i, takePhotoData);
                }
            }
            List<Map.Entry<Integer, DocxTakePhotoLegendReplaceOption.TakePhotoData>> list = new ArrayList<>(indexMap.entrySet());
            builder.moveTo(replacingArgs.getMatchNode());
            if (CollectionUtil.isNotEmpty(list)) {
                Paragraph paragraph = null;
                for (int i = 0; i < list.size(); i++) {
                    DocxTakePhotoLegendReplaceOption.TakePhotoData d = list.get(i).getValue();
                    if (i == 0){
                        paragraph = new Paragraph(builder.getDocument());
                        paragraph.getParagraphFormat().setAlignment(ParagraphAlignment.CENTER);
                        builder.getCurrentParagraph().getParentNode().insertBefore(paragraph, builder.getCurrentParagraph());
                    }
                    builder.moveTo(paragraph);
                    builder.insertBreak(BreakType.PAGE_BREAK);
                    String tf = "取证图片%d(%s %s)";
                    builder.write(String.format(tf, list.get(i).getKey() + 1, d.getCreator(), d.getTime()));
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
