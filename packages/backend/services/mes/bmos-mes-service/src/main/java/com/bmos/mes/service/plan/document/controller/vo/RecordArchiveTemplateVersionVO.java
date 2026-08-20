package com.bmos.mes.service.plan.document.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 生产批号的工艺绑定了哪些模板
 */
@Getter
@Setter
@ApiModel("生产批号的工艺绑定了哪些模板VO")
public class RecordArchiveTemplateVersionVO {

    /**
     * 生产计划id
     */
    @ApiModelProperty("生产计划id")
    private Long planId;

    /**
     * 模板id
     */
    @ApiModelProperty("模板id")
    private Long templateInfoId;

    /**
     * 批记录模板名称
     */
    @ApiModelProperty("批记录模板名称")
    private String templateName;

    /**
     * 该模板生成的生效的批记录发记录id
     */
    @ApiModelProperty("该模板生成的生效的批记录id")
    private Long effectiveArchiveRecordId;

    /**
     * 批记录模板版本
     */
    @ApiModelProperty("批记录模板版本")
    private String version;

    /**
     * 生效的批记录编号
     */
    @ApiModelProperty("生效的批记录编号")
    private String effectiveNo;

    /**
     * 生成人
     */
    @ApiModelProperty("生成人")
    private String operatorName;

    /**
     * 生成时间
     */
    @ApiModelProperty("生成时间")
    private LocalDateTime archiveTime;

    /**
     * 生效时间
     */
    @ApiModelProperty("生效时间")
    private LocalDateTime effectiveTime;

    /**
     * 文件路径
     */
    @ApiModelProperty("文件路径")
    private String path;

    /**
     * 批记录id
     */
    @ApiModelProperty("批记录id")
    private Long archiveId;
}
