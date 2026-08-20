package com.bmos.mes.service.weigh.centre2.execute.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@ApiModel("工单称量记录分页VO")
public class TicketWeighRequirementRecordVO {
    @ApiModelProperty("工单ID")
    private Long ticketId;

    @ApiModelProperty("工单编号")
    private String ticketNo;

    @ApiModelProperty("称量中心编码")
    private String centreCode;

    @ApiModelProperty("称量中心名称")
    private String centreName;

    @ApiModelProperty("称量中心绑定的工位")
    private List<Long> stationIdList;

    @ApiModelProperty("称量中心绑定的数据权限部门ID")
    private List<Long> deptIdList;

    @ApiModelProperty("称量人id")
    private String weighUserId;

    @ApiModelProperty("称量人姓名")
    private String weighUserName;

    @ApiModelProperty("称量登录名")
    private String weighUserLoginName;

    @ApiModelProperty("复核人id")
    private String signUserId;

    @ApiModelProperty("复核人登录名")
    private String signUserLoginName;

    @ApiModelProperty("复核人")
    private String signUserName;

    @ApiModelProperty("下发时间")
    private LocalDateTime sendTime;

    @ApiModelProperty("完成时间")
    private LocalDateTime completeTime;

    @ApiModelProperty("称量记录数据")
    private List<TicketWeighRecordVO> recordVOList;

    @ApiModelProperty("余料称量记录数据")
    private List<TicketWeighRecordVO> oddmentRecordVOList;
} 