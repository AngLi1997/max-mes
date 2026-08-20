package com.bmos.lims2.server.report.dto;

import com.bmos.lims2.common.enums.ReportOperationTypeEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("报告操作历史DTO")
public class ReportOperationHistoryDTO {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("报告任务ID")
    private Long taskId;
    @ApiModelProperty("操作类型")
    private ReportOperationTypeEnum operationType;
    @ApiModelProperty("操作人ID")
    private String operatorId;
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    @ApiModelProperty("操作时间")
    private LocalDateTime operateTime;
    @ApiModelProperty("下载地址（bucket/object）")
    private String path;
    @ApiModelProperty("备注")
    private String remark;
    private String createBy;
    private String createUserName;

    public void getCreateUserName(String createUserName) {
        UserUtils.getUser(createBy);
    }
}


