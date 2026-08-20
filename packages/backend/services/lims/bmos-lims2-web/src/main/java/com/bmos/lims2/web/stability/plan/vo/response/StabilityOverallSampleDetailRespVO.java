package com.bmos.lims2.web.stability.plan.vo.response;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 稳定性整体样品详情响应VO（批次维度）
 */
@Data
@ApiModel("稳定性整体样品详情")
public class StabilityOverallSampleDetailRespVO {

    // ── 基础信息 ──────────────────────────────────────────────

    @ApiModelProperty("稳定性考察计划ID")
    private Long planId;

    @ApiModelProperty("稳定性考察计划编号")
    private String planCode;

    @ApiModelProperty("批次ID")
    private Long batchId;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("批次生产日期")
    private LocalDate productionDate;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("检品单位ID")
    private Long materialUnitId;

    @ApiModelProperty("检品单位名称")
    private String materialUnitName;

    @ApiModelProperty("稳定性考察方案名称")
    private String schemeName;

    @ApiModelProperty("方案编码")
    private String schemeCode;

    @ApiModelProperty("方案版本号")
    private String schemeVersionNo;

    @ApiModelProperty("计划备注")
    private String remark;

    @ApiModelProperty("计划创建人ID")
    private String planCreateBy;

    @ApiModelProperty("计划创建时间")
    private LocalDateTime planCreateTime;

    @ApiModelProperty("计划创建人名称（姓名-loginName）")
    private String planCreateByUserName;

    public String getPlanCreateByUserName() {
        BaseUserDO user = UserUtils.getUser(planCreateBy);
        if (ObjectUtil.isEmpty(user)) {
            return "";
        }
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }

    // ── 取样信息列表 ──────────────────────────────────────────

    @ApiModelProperty("样品列表（每个试验类型一行）")
    private List<SampleItemRespVO> samples;

    @Data
    @ApiModel("稳定性整体样品-取样信息行")
    public static class SampleItemRespVO {

        @ApiModelProperty("样品记录ID")
        private Long id;

        @ApiModelProperty("样品编号")
        private String sampleNo;

        @ApiModelProperty("试验类型")
        private String experimentType;

        @ApiModelProperty("试验类型名称")
        private String experimentTypeName;

        @ApiModelProperty("试验储存条件")
        private String storageCondition;

        @ApiModelProperty("计划取样量")
        private String plannedSampleAmount;

        @ApiModelProperty("实际取样量")
        private String actualSampleAmount;

        @ApiModelProperty("取样量单位")
        private String sampleUnit;

        @ApiModelProperty("取样量单位名称")
        private String unitName;

        @ApiModelProperty("是否手动新增（true=可删除，false=来源于方案不可删除）")
        private boolean manualAdded;

        @ApiModelProperty("样品状态")
        private StabilityPlanSampleStatusEnum status;

        @ApiModelProperty("取样人ID")
        private String samplerId;

        @ApiModelProperty("取样人姓名")
        private String samplerName;

        @ApiModelProperty("取样时间")
        private LocalDateTime samplingTime;

        @ApiModelProperty("接收人ID")
        private String receiverId;

        @ApiModelProperty("接收人姓名")
        private String receiverName;

        @ApiModelProperty("接收时间")
        private LocalDateTime receiveTime;

        @ApiModelProperty("样品接收日期")
        private LocalDate receiveDate;

        @ApiModelProperty("储存位置")
        private String storageLocation;
    }
}
