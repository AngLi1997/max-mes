package com.bmos.lims2.server.inspect.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: APP 分析项-方法分组（母列表）
 * @Author: yigaohui
 * @Date: 2025/11/27 00:00
 */
@Getter
@Setter
@ApiModel("APP-分析项-方法分组（母列表）")
public class AppAnalysisItemMethodGroupDTO {

    @ApiModelProperty("分析项ID")
    private Long inspectParameterId;

    @ApiModelProperty("分析项名称")
    private String inspectParameterName;

    @ApiModelProperty("分析项编码")
    private String inspectParameterCode;

    @ApiModelProperty("方法ID")
    private Long recordId;

    @ApiModelProperty("方法版本ID")
    private Long recordVersionId;

    @ApiModelProperty("方法名称")
    private String recordName;

    @ApiModelProperty("方法编码")
    private String recordCode;

    @ApiModelProperty("方法版本号")
    private String recordVersionNo;

    @ApiModelProperty("该分组下的检验单任务列表（子列表）")
    private List<AppTaskEntryItemDTO> inspectionTasks;
}

