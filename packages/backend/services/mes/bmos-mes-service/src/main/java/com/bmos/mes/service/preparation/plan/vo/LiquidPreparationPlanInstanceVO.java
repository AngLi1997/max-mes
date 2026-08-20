package com.bmos.mes.service.preparation.plan.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("配液计划组件实例")
@Data
public class LiquidPreparationPlanInstanceVO {

    @ApiModelProperty("配液单名称")
    private String name;

    @ApiModelProperty("组件实例id/配液单id")
    private Long id;

    @ApiModelProperty("产出中间品名称")
    private String materialName;

    @ApiModelProperty("产出中间品合并编码")
    private String materialMergeCode;

    @ApiModelProperty("目标体积")
    private String targetVolume;

    @ApiModelProperty("配液物料列表")
    private List<LiquidPreparationPlanMaterialVO> materialList;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("配液计划是否完成")
    private Boolean completed;

    @ApiModelProperty("是否有配置")
    private boolean noConfig;

}
