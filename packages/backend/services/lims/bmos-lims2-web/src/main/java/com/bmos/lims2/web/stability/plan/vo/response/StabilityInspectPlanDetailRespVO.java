package com.bmos.lims2.web.stability.plan.vo.response;

import com.bmos.lims2.common.enums.StabilityInspectPlanStatusEnum;
import com.bmos.lims2.server.stability.plan.dto.response.StabilityInspectPlanMatrixDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性考察计划详情响应VO
 */
@Data
@ApiModel("稳定性考察计划详情响应")
public class StabilityInspectPlanDetailRespVO {

    @ApiModelProperty("计划ID")
    private Long id;

    @ApiModelProperty("考察计划编号")
    private String code;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("稳定性方案ID")
    private Long schemeId;

    @ApiModelProperty("稳定性方案编码")
    private String schemeCode;

    @ApiModelProperty("稳定性方案名称")
    private String schemeName;

    @ApiModelProperty("稳定性方案版本ID")
    private Long schemeVersionId;

    @ApiModelProperty("稳定性方案版本号")
    private String schemeVersionNo;

    @ApiModelProperty("计划状态（PENDING/IN_PROGRESS/COMPLETED/PAUSED）")
    private StabilityInspectPlanStatusEnum status;

    @ApiModelProperty("开始时间")
    private LocalDate startDate;

    @ApiModelProperty("计划结束时间")
    private LocalDate planEndDate;

    @ApiModelProperty("暂停时间")
    private LocalDateTime pauseTime;

    @ApiModelProperty("暂停操作人姓名")
    private String pauseByName;

    @ApiModelProperty("暂停理由")
    private String pauseReason;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建人名称（格式：姓名-登录名）")
    private String createByUserName;

    @ApiModelProperty("试验类型名称（多个用逗号隔开）")
    private String experimentTypeNames;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("样品开始接收时间（第一个整体样品的接收时间）")
    private LocalDateTime sampleFirstReceiveTime;

    @ApiModelProperty("样品结束接收时间（最后一个整体样品的接收时间）")
    private LocalDateTime sampleLastReceiveTime;

    @ApiModelProperty("批次列表")
    private List<BatchVO> batches;

    @ApiModelProperty("矩阵数据（试验类型 × 批次 × 时间点）")
    private StabilityInspectPlanMatrixDTO matrix;

    @Data
    @ApiModel("批次信息响应")
    public static class BatchVO {
        @ApiModelProperty("批次ID")
        private Long id;

        @ApiModelProperty("批号")
        private String batchNo;

        @ApiModelProperty("批次生产日期")
        private LocalDate productionDate;

        @ApiModelProperty("样品接收日期")
        private LocalDate sampleReceiveDate;

        @ApiModelProperty("排序")
        private Integer sort;
    }
}
