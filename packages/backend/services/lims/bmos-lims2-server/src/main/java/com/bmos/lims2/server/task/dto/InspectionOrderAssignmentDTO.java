package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单分配DTO（母列表）
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class InspectionOrderAssignmentDTO {

    /**
     * 检验单ID
     */
    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    /**
     * 检验单编号
     */
    @ApiModelProperty("检验单编号")
    private String orderNo;

    /**
     * 检品名称
     */
    @ApiModelProperty("检品名称")
    private String materialName;

    /**
     * 检品编码
     */
    @ApiModelProperty("检品编码")
    private String materialCode;

    /**
     * 检品规格
     */
    @ApiModelProperty("检品规格")
    private String materialSpec;

    /**
     * 批号
     */
    @ApiModelProperty("批号")
    private String batchNo;

    /**
     * 请验时间
     */
    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    /**
     * 请验人ID
     */
    @ApiModelProperty("请验人ID")
    private String requestUserId;

    /**
     * 请验人名称
     */
    @ApiModelProperty("请验人名称")
    private String requestUserName;

    /**
     * 该检验单下的任务列表（子列表）
     * 默认展开
     */
    @ApiModelProperty("该检验单下的任务列表")
    private List<TaskDTO> tasks;

    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }
}
