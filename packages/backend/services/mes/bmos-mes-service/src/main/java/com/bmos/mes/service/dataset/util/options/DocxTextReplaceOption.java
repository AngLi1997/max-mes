package com.bmos.mes.service.dataset.util.options;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.ReplaceAction;
import com.bmos.mes.service.dataset.common.DatasetTransValueData;

/**
 * docx渲染器（文本）
 * @author liang
 * @version 1.0.0
 * @date 2024/8/21 15:27
 */
public class DocxTextReplaceOption extends FindReplaceOptions {
    public DocxTextReplaceOption(DocumentBuilder builder, DatasetTransValueData data){
        setLegacyMode(false);
        setReplacingCallback(replacingArgs -> {
            builder.moveTo(replacingArgs.getMatchNode());
            replacingArgs.setReplacement(replacingArgs.getReplacement());
            return ReplaceAction.REPLACE;
        });
    }
}
