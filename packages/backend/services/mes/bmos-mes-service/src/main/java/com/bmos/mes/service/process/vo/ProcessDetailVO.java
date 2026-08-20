package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工艺详情VO")
public class ProcessDetailVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("产品分类id")
    private Long productCategoryId;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("流程模型id")
    private String processModelId;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("配方版本id")
    private Long productFormulaVersionId;

    @ApiModelProperty("版本id")
    private Long processVersionId;

    @ApiModelProperty("产线id")
    private List<Long> productionLineIds;

    @ApiModelProperty("生产阶段代码")
    private String productionStageCode;

    @ApiModelProperty("关联的批记录")
    private List<RelationBatchRecordItemVO> batchRecordItems;

    @ApiModelProperty("工序信息")
    private List<ProcedureVO> procedures;

}
