package com.bmos.mes.service.output.finished.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@TableName("bm_output_finished_product_result")
public class FinishedProductOutputResult extends BaseDO {

    /**
     * output_finished_product_id
     */
    private Long outputFinishedProductId;

    /**
     * 成品id
     */
    private Long productId;

    /**
     * 成品编码
     */
    private String productMergeCode;

    /**
     * 成品名称
     */
    private String productName;

    /**
     * 成品批号
     */
    private String productBatchNo;

    /**
     * 成品规格
     */
    private String specification;

    /**
     * 单件量
     */
    private BigDecimal singleQuantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 件数
     */
    private Integer number;

    /**
     * 操作人id
     */
    private String operatorId;

}
