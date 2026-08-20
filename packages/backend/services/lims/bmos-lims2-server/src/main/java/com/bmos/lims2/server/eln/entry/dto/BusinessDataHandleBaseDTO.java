package com.bmos.lims2.server.eln.entry.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("业务组件数据处理基础DTO")
@Data
public class BusinessDataHandleBaseDTO {

    @ApiModelProperty("组件id")
    @NotNull
    private Long componentId;

    @ApiModelProperty("任务id")
    private Long taskId;

    /**
     * 生产计划id
     */
    @ApiModelProperty(value = "生产计划id",required = true)
    @NotNull
    private Long inspectionOrderId;

    /**
     * 批号
     */
    @NotEmpty
    @ApiModelProperty(value = "批号",required = true)
    private String batchNo;


    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long schemeId;

    /**
     * 工艺版本
     */
    @ApiModelProperty(value = "工艺版本号",required = true)
    @NotNull
    private Long schemeVersionId;

    /**
     * 记录项id
     */
    @ApiModelProperty(value = "记录项id",required = true)
    @NotNull
    private Long recordItemId;

    @ApiModelProperty(value = "记录id",required = true)
    private Long recordId;


    /**
     * 记录项版本id
     */
    @ApiModelProperty(value = "记录项版本id",required = true)
    @NotNull
    private Long recordVersionId;


    @ApiModelProperty(value = "检验项目Id")
    private Long itemId;

    @ApiModelProperty(value = "检验项目配置id")
    private Long itemConfigId;

    @ApiModelProperty(value = "检验分析项id")
    private Long parameterId;

    @ApiModelProperty(value = "检验分析项配置id")
    private Long parameterConfigId;

}
