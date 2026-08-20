package com.bmos.mes.service.plan.info.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/6 10:00
 */
@Data
public class ProductPlanRelationTreeNodeVO {

    private Long productPlanId;

    private Long relationProductPlanId;

    private List<ProductPlanRelationTreeNodeVO> children;
}
