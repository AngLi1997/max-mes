package com.bmos.lims2.server.recordprint.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 记录项打印资源DTO（模板与页眉页脚）
 * @Author: yigaohui
 * @Date: 2025/11/25 12:10
 */
@Getter
@Setter
public class RecordItemAssetsDTO {

    /**
     * 模板HTML
     */
    private String templateContent;

    /**
     * 页眉配置JSON
     */
    private String docxHeader;

    /**
     * 页脚配置JSON
     */
    private String docxFooter;

    /**
     * 记录项ID
     */
    private Long recordItemId;

    /**
     * 记录版本ID
     */
    private Long recordVersionId;

    /**
     * 页面配置JSON（包含横版/纸张等）
     */
    private String pageConfig;

    /**
     * 首页不同
     */
    private Boolean firstDifferent;

    /**
     * 奇偶不同
     */
    private Boolean oddAndEvenDifferent;

    /**
     * 页码样式（可选）
     */
    private Integer pageNumberStyle;

    /**
     * 页码起始值（可选）
     */
    private Integer pageStartingNumber;
}



