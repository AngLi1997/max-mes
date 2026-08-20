package com.bmos.lims2.server.inspect.entry.dto;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单录入查询DTO
 *
 * @author system
 * @since 2025/01/30
 */
@Getter
@Setter
@ApiModel("检验单录入查询对象")
public class InspectionOrderEntryQueryDTO extends BasePage {

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("分析项ID")
    private Long parameterId;

    @ApiModelProperty("检品信息ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("检验单号")
    private String inspectionOrderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("请验开始时间")
    private LocalDateTime requestStartTime;

    @ApiModelProperty("请验结束时间")
    private LocalDateTime requestEndTime;

    @ApiModelProperty("任务状态列表")
    private java.util.List<TaskStatusEnum> taskStatuses;

    @ApiModelProperty("是否判定异常")
    private Boolean abnormal;

    @ApiModelProperty("仅查询待复核且未复核的任务（reviewed_result 为空）")
    private Boolean unreviewedOnly;


    @ApiModelProperty("仅查询当前用户的任务")
    private Boolean ownerOnly;

    @ApiModelProperty("当前用户ID（用于数据权限控制）")
    private String currentUserId;

    @ApiModelProperty("当前用户所在的检验班组ID列表（用于数据权限控制）")
    private List<Long> currentUserTeamIds;

    @ApiModelProperty("执行方式过滤：LIMS/ELN；APP端固定设置为ELN")
    private ExecuteMethodEnum executeMethod;
}
