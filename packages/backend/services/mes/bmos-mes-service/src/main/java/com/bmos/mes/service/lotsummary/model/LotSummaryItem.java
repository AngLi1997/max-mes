package com.bmos.mes.service.lotsummary.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mes.service.lotsummary.enums.LotSummaryItemType;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 批次摘要关联数据
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bm_lot_summary_item")
public class LotSummaryItem extends BaseDO {

    /**
     * 批次摘要id
     */
    private Long lotSummaryId;

    /**
     * 标题名称
     */
    private String labelName;

    /**
     * 批次摘要项目类型
     */
    private LotSummaryItemType lotSummaryItemType;

    /**
     * 数据点
     */
    private Long datasetPointId;
}
