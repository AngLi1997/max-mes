package com.bmos.mes.service.workflow.vo;

import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@ApiModel("生产计划辅助记录VO")
@Data
public class PlanSubRecordVO {

    private Long id;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("工步名称")
    private String procedureStepName;

    @ApiModelProperty("工步模型id")
    private Long procedureStepModelId;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("完成人id")
    private String completeUserId;

    @ApiModelProperty("完成人名称")
    private String completeUserName;

    @ApiModelProperty("工艺换班次数")
    private Integer processChangeNumber;

    @ApiModelProperty("工序换班次数")
    private Integer procedureChangeNumber;

    @ApiModelProperty("归档pdf文件地址")
    private String archiveUrl;

    public String getCompleteUserName() {
        if (StrUtil.isEmpty(completeUserId)) {
            return StrUtil.EMPTY;
        }
        BaseUserDO user = UserUtils.getUser(completeUserId);
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }

    public Integer getProcessChangeNumber() {
        return processChangeNumber == null ? 0 : processChangeNumber + 1;
    }

    public Integer getProcedureChangeNumber() {
        return procedureChangeNumber == null ? 0 : procedureChangeNumber + 1;
    }

}
