package com.bmos.mes.service.plan.document.controller.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.plan.BatchRecordArchiveStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 生产批次下的批记录档案
 */
@Getter
@Setter
@ApiModel("生产批次下的批记录档案VO")
public class BatchRecordArchiveVO {

    /**
     * 批记录档案id
     */
    @ApiModelProperty("批记录档案id")
    private Long archiveId;

    /**
     * 生成的批记录编号
     */
    @ApiModelProperty("生成批记录编号")
    private String archiveNo;

    /**
     * 批记录档案路径
     */
    @ApiModelProperty("批记录档案路径")
    private String path;

    /**
     * 模板版本
     */
    @ApiModelProperty("模板版本")
    private String templateVersion;

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
     * 状态
     */
    @ApiModelEnumProperty(value = "操作名称", enumClass = BatchRecordArchiveStatusEnum.class)
    @EnumValidate(BatchRecordArchiveStatusEnum.class)
    private BatchRecordArchiveStatusEnum status;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 审批流实例id
     */
    @ApiModelProperty("审批流实例id")
    private String instanceId;

}

