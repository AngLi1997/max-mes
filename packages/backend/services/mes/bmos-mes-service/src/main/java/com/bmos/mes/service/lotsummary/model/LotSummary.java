package com.bmos.mes.service.lotsummary.model;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("bm_lot_summary")
public class LotSummary extends BaseDO {

    private String name;

    private Long productId;

    private String productName;

    private Long processId;

    private String processName;
}
