package com.bmos.mes.service.plan.info.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 批记录/批签发分页查询条件
 */
@Getter
@Setter
@ApiModel("批记录/批签发分页查询DTO")
public class PlanBatchPageDTO extends BasePage {

    /**
     * 产品分类id
     */
    @ApiModelProperty(value = "产品分类id")
    private Long productCategoryId;

    /**
     * 产品物料id
     */
    @ApiModelProperty(value = "产品物料id")
    private Long productMaterialId;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号")
    private String batchNo;

    /**
     * 生产工艺
     */
    @ApiModelProperty("生产工艺")
    private String processName;

}
