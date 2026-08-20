package com.bmos.mes.service.plan.info.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.common.enums.execute.ExceptionRecordModeEnum;
import com.bmos.mes.common.enums.execute.ExceptionStatusEnum;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 批次追溯-批次偏差信息
 */
@ApiModel("批次追溯-批次偏差信息")
@Data
public class PlanRetraceDeviationPageVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("异常类型")
    private String exceptionType;

    @ApiModelProperty("异常类型code")
    private String exceptionTypeCode;

    @ApiModelProperty("异常描述")
    private String exceptionDescription;

    @ApiModelEnumProperty(value = "异常状态", enumClass = ExceptionStatusEnum.class)
    private ExceptionStatusEnum exceptionStatus;

    @ApiModelEnumProperty(value = "记录方式", enumClass = ExceptionRecordModeEnum.class)
    private ExceptionRecordModeEnum recordMode;

    @ApiModelProperty("记录人")
    private String recordUserId;

    @ApiModelProperty("记录人 名称-code")
    private String recordUserName;

    @ApiModelProperty("记录时间")
    private LocalDateTime recordTime;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("产品名称")
    private String productFullName;

    @ApiModelProperty("产品合并编码")
    private String productMergeCode;

    @ApiModelProperty("生产计划id")
    private Long productPlanId;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;


    @ApiModelProperty("工步id")
    private Long procedureStepId;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("处理人id")
    private String handleUserId;

    @ApiModelProperty("处理结果")
    private String handleResult;

    @ApiModelProperty("处理时间")
    private LocalDateTime handleTime;

    @ApiModelProperty("处理人名称")
    private String handleUserName;

    @ApiModelProperty("作废人id")
    private String cancelUserId;

    @ApiModelProperty("作废人名称")
    private String cancelUserName;

    @ApiModelProperty("作废原因")
    private String cancelReason;

    @ApiModelProperty("作废时间")
    private LocalDateTime cancelTime;

    @ApiModelProperty("创建人id")
    private String createBy;

    @ApiModelProperty("创建人名称")
    private String createByUserName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工步/任务 名称")
    private String procedureStepName;

    public String getCreateByUserName() {
        BaseUserDO user = UserUtils.getUser(createBy);
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }

}
