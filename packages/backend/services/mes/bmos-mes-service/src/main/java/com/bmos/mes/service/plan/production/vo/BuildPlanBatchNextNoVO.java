package com.bmos.mes.service.plan.production.vo;

import com.bmos.mes.service.product.vo.MaterialFieldInfoVO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @ClassName BuildPlanBatchNextNoVO
 * @Description 构建批号所需字段信息
 * @Author Ren Jin Guang
 * @Date 2024/8/28 16:15
 */
@Setter
@Getter
@ToString
public class BuildPlanBatchNextNoVO {

    @ApiModelProperty("物料id")
    private Long materialId;

    @ApiModelProperty(value = "内包规格")
    private String innerPackingSpecification;

    @ApiModelProperty("包装规格")
    private String packingSpecification;

    @ApiModelProperty("产品标识")
    private String productMark;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品阶段代码")
    private String productionStageCode;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("物料信息自定义字段")
    private List<MaterialFieldInfoVO> customFieldInfoList;
}
