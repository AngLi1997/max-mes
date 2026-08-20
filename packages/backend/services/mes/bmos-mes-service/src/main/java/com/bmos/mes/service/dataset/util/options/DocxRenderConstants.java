package com.bmos.mes.service.dataset.util.options;

/**
 * 批记录生成占位符
 * @author liang
 * @version 1.0.0
 * @date 2024/9/26 11:47
 */
public class DocxRenderConstants {

    public static final int TOP_MARGIN = 100;

    /**
     * 拍照取证占位符
     */
    public static final String EVIDENCE_PLACEHOLDER = "${<evidence_photo>}";

    /**
     * 照相组件占位符
     */
    public static final String TAKE_PHOTO_PLACEHOLDER = "${<take_photo>}";

    /**
     * 拍照取证占位符2 工艺名称 工序名称 批次顺序 工艺换班 工序换班
     * ${<evidence_photo>[工艺a][工序1][0][0][0]}
     * 默认配置 读不到系统配置则使用这个
     */
    public static final String EVIDENCE_PLACEHOLDER2_DEFAULT = "\\$\\{<evidence_photo>(\\[[\\u4e00-\\u9fa5a-zA-Z0-9#()（）%.*,，、\\-\\s]+]){2}(\\[\\d*]){3}}";

    /**
     * 照相组件占位符2 工艺名称 工序名称 批次顺序 工艺换班 工序换班
     * ${<take_photo>[工艺a][工序1][0][0][0]}
     * 默认配置 读不到系统配置则使用这个
     */
    public static final String TAKE_PHOTO_PLACEHOLDER2_DEFAULT = "\\$\\{<take_photo>(\\[[\\u4e00-\\u9fa5a-zA-Z0-9#()（）%.*,，、\\-\\s]+]){2}(\\[\\d*]){3}}";
}
