package com.bmos.mes.service.dataset.util.options;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.ReplaceAction;

/**
 * docx渲染器（错误）
 * @author liang
 * @version 1.0.0
 * @date 2024/8/21 15:27
 */
public class DocxErrorReplaceOption extends FindReplaceOptions {
    public DocxErrorReplaceOption(DocumentBuilder builder){
        setLegacyMode(false);
        setReplacingCallback(replacingArgs -> {
            builder.moveTo(replacingArgs.getMatchNode());
            return ReplaceAction.SKIP;
        });
    }
}
