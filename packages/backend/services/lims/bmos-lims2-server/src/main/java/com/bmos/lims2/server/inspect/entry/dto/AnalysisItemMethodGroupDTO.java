package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 分析项-方法分组（母列表）DTO
 * @Author: yigaohui
 * @Date: 2025/11/17 00:00
 */
@Getter
@Setter
@ApiModel("分析项-方法分组（母列表）")
public class AnalysisItemMethodGroupDTO {

    @ApiModelProperty("分析项ID")
    private Long inspectParameterId;

    @ApiModelProperty("分析项名称")
    private String inspectParameterName;

    @ApiModelProperty("分析项编码")
    private String inspectParameterCode;

    @ApiModelProperty("方法ID（ELN为batch_record.id，LIMS为inspect_parameter_record.id）")
    private Long recordId;

    @ApiModelProperty("方法版本ID（同版本ID视为同一方法）")
    private Long recordVersionId;

    @ApiModelProperty("方法名称（recordName）")
    private String recordName;

    @ApiModelProperty("方法编码（recordCode）")
    private String recordCode;

    @ApiModelProperty("方法版本号（recordVersionNo）")
    private String recordVersionNo;

    @ApiModelProperty("该分析项+方法版本下的检验单任务列表（子列表）")
    private List<AnalysisItemEntryDTO.InspectionOrderEntryItemDTO> inspectionTasks;
}


