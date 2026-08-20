package com.bmos.mes.service.trace.material.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/21 10:10
 */
@Data
@ApiModel(value = "物料追踪信息VO")
public class MaterialTraceVO {

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    @ApiModelProperty(value = "生产批次号", example = "123456789")
    private String batchNo;

    @ApiModelProperty(value = "产品名称", example = "产品名称")
    private String productName;

    @ApiModelProperty(value = "合并编码", example = "合并编码")
    private String mergeCode;

    @ApiModelProperty(value = "物料规格", example = "物料规格")
    private String materialSpecification;

    @ApiModelProperty(value = "产线名称", example = "产线1")
    private String productionLineName;

    @ApiModelProperty(value = "生产开始时间", example = "2024-11-21 10:10:10")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "生产结束时间", example = "2024-11-21 10:10:10")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    @ApiModelProperty(value = "工艺名称", example = "工艺名称")
    private String processName;

    @ApiModelProperty(value = "工艺版本", example = "1")
    private String processVersion;

    @ApiModelProperty("树")
    private List<MaterialTraceMaterialView> traceTree;
}
