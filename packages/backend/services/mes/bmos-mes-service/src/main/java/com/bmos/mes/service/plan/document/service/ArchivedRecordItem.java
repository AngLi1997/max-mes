package com.bmos.mes.service.plan.document.service;

import com.aspose.words.Document;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 归档返回数据
 */
@Data
@Accessors(chain = true)
public class ArchivedRecordItem {
    private Long planId;
    private Long recordItemId;
    private Long copyVersion;
    private String wordUrl;
    private Long procedureStepModelId;
    private Long procedureStepId;
    private Document document;

    /**
     * 工艺换班次数
     */
    private Integer processChangeNumber;

    /**
     * 工序换班次数
     */
    private Integer procedureChangeNumber;

    /**
     * 是否复用
     */
    private Boolean reuse;


    /**
     * 记录项配置的排序
     */
    private Long itemConfigArchiveOrder;

    /**
     * 是否是复制出来的记录
     */
    private Boolean copyItem;

    /**
     * 复制版本id
     */
    private Long copyVersionId;

    /**
     * 是否作废
     */
    private Boolean discard;

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 工序模型id
     */
    private Long procedureModelId;

}
