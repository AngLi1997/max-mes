package com.bmos.mes.service.dataset.util.options;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.ReplaceAction;
import com.aspose.words.Shape;
import com.bmos.mes.service.dataset.common.DatasetTransValueData;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * docx渲染器（图片）
 * @author liang
 * @version 1.0.0
 * @date 2024/8/21 15:27
 */
public class DocxImageReplaceOption extends FindReplaceOptions {

    public DocxImageReplaceOption(DocumentBuilder builder, DatasetTransValueData data, Map<String, byte[]> imageMap, String recordEmpty){
        setLegacyMode(false);
        setReplacingCallback(replacingArgs -> {
            builder.moveTo(replacingArgs.getMatchNode());
            String replacement = replacingArgs.getReplacement();
            if (StringUtils.equals(recordEmpty, replacement) || data.isEmpty()){
                replacingArgs.setReplacement(replacement);
                return ReplaceAction.REPLACE;
            }
            String[] split = replacement.split(",");
            for (String url : split) {
                byte[] bytes = imageMap.get(url);
                if (bytes != null && bytes.length != 0){
                    Shape shape = builder.insertImage(bytes, 100, 100);
                    builder.moveTo(shape);
                }
            }
            replacingArgs.setReplacement("");
            return ReplaceAction.REPLACE;
        });
    }
}
