package com.bmos.mes.service.dataset.util.options;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aspose.words.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * docx渲染器（拍照组件图例）
 * @author liang
 * @version 1.0.0
 * @date 2024/8/21 15:27
 */
public class DocxTakePhotoLegendReplaceOption extends FindReplaceOptions {

    Logger log = LoggerFactory.getLogger(DocxTakePhotoLegendReplaceOption.class);

    public DocxTakePhotoLegendReplaceOption(DocumentBuilder builder, AtomicInteger index, LinkedHashMap<String, TakePhotoData> takePhotoMap){
        setLegacyMode(false);
        setReplacingCallback(replacingArgs -> {
            String replacement = replacingArgs.getReplacement();
            try {
                List<TakePhotoData> takePhotoData = JSON.parseArray(replacement, TakePhotoData.class);
                for (TakePhotoData data : takePhotoData) {
                    // 图片信息
                    index.incrementAndGet();
                    builder.moveTo(replacingArgs.getMatchNode());
                    builder.insertBreak(BreakType.PARAGRAPH_BREAK);
                    builder.getParagraphFormat().setAlignment(ParagraphAlignment.LEFT);
                    String tf = "图例%d(%s %s)";
                    String legend = String.format(tf, index.get(), data.getCreator(), data.getTime());
                    if (StrUtil.isNotEmpty(data.getRemark())) {
                        legend = legend + "\n备注信息:" + data.getRemark();
                    }
                    builder.write(legend);
                    takePhotoMap.put(legend, data);
                }
                replacingArgs.setReplacement("");
            }catch (Exception e){
                log.error("拍照组件替换失败", e);
            }
            return ReplaceAction.REPLACE;
        });
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TakePhotoData {

        private Long id;

        /**
         * 图片地址
         */
        private String imageUrl;
        private String creator;
        private String time;

        /**
         * 工艺名称
         */
        private String processName;

        /**
         * 工序名称
         */
        private String procedureName;

        /**
         * 批次顺序
         */
        private String batchIndex = "0";

        /**
         * 工艺换班次数
         */
        private Integer processChangeNumber;

        /**
         * 工序换班次数
         */
        private Integer procedureChangeNumber;
        private String remark;
    }



}
