package com.bmos.lims2.server.eln.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("附件查询")
@ToString
public class ExecuteAttachmentQueryDTO {



    @ApiModelProperty(value = "文件类型")
    private String type;

    @ApiModelProperty(value = "附件类型")
    private String attachmentType;

    @ApiModelProperty("请验单id")
    private Long inspectionOrderId;

    @ApiModelProperty(value = "请验单编号")
    private String inspectionOrderNo;
    /**
     * 批号
     */
    @ApiModelProperty(value = "批号",required = true)
    @NotEmpty
    private String batchNo;


    @ApiModelProperty
    private Long taskId;

    @ApiModelProperty(value = "方法id",required = true)
    @NotNull
    private Long recordId;

    @ApiModelProperty(value = "方法版本id",required = true)
    private Long recordVersionId;

    @ApiModelProperty(value = "方案id",required = true)
    private Long schemeId;

    @ApiModelProperty(value = "方案版本id",required = true)
    private Long schemeVersionId;

    @ApiModelProperty(value = "分析项配置id",required = true)
    private Long parameterConfigId;
}
