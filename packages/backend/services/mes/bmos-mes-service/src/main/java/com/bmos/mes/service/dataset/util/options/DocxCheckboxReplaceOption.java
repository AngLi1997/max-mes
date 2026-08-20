package com.bmos.mes.service.dataset.util.options;

import cn.hutool.core.util.StrUtil;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FindReplaceOptions;
import com.aspose.words.ReplaceAction;
import com.aspose.words.Run;
import com.bmos.mes.service.dataset.common.DatasetTransValueData;

import java.util.Objects;

/**
 * docx渲染器（勾选）
 * @author liang
 * @version 1.0.0
 * @date 2024/8/21 15:27
 */
public class DocxCheckboxReplaceOption extends FindReplaceOptions {
    public DocxCheckboxReplaceOption(DocumentBuilder builder, DatasetTransValueData data, String placeHolder){
        setLegacyMode(false);
        setReplacingCallback(replacingArgs -> {
            if (replacingArgs.getMatchNode() != null && replacingArgs.getMatchNode().getNextSibling() != null){
                builder.moveTo(replacingArgs.getMatchNode().getNextSibling());
            }else {
                builder.moveTo(replacingArgs.getMatchNode());
            }
            builder.getFont().setName("Wingdings 2");
            // 判断是否是录入空值
            if (Objects.nonNull(data) && data.isEmpty()) {
                // 代表word中的方框加×
                builder.write(" \u0053");
                replacingArgs.setReplacement(" ");
            } else if (StrUtil.equals(replacingArgs.getReplacement(), "true")){
                // 代表word中的勾选
                builder.write(" \u0052");
                replacingArgs.setReplacement(" ");
            }else {
                // 无值也回填方框加×
                builder.write(" \u0053");
                replacingArgs.setReplacement(" ");
            }
            return ReplaceAction.REPLACE;
        });
    }

    /**
     * 拆分 Run 节点
     *
     * @param run      要拆分的 Run 节点
     * @param position 拆分位置
     * @return 拆分后的第二部分 Run
     * @throws Exception 异常
     */
    private static Run splitRun(DocumentBuilder builder, Run run, int position) throws Exception {
        String text = run.getText();
        Run newRun = (Run) run.deepClone(true);
        builder.getDocument().importNode(newRun, true);
        newRun.setText(text.substring(position));
        run.setText(text.substring(0, position));
        run.getParentNode().insertAfter(newRun, run);
        return newRun;
    }
}
