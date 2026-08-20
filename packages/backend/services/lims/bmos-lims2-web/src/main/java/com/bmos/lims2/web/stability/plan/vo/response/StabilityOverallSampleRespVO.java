package com.bmos.lims2.web.stability.plan.vo.response;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.StabilityPlanSampleStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性整体样品列表响应VO（批次维度）
 */
@Data
@ApiModel("稳定性整体样品列表")
public class StabilityOverallSampleRespVO {

    @ApiModelProperty("批次ID（用于跳转取样详情）")
    private Long batchId;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("批次生产日期")
    private LocalDate productionDate;

    @ApiModelProperty("稳定性考察计划ID")
    private Long planId;

    @ApiModelProperty("稳定性考察计划编号")
    private String planCode;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("计划创建人ID")
    private String planCreateBy;

    @ApiModelProperty("计划创建时间")
    private LocalDateTime planCreateTime;

    @ApiModelProperty("计划创建人名称（姓名-loginName）")
    private String planCreateByUserName;

    @ApiModelProperty("批次样品状态：PENDING=待取样，SAMPLED=已取样，RECEIVED=已接收")
    private StabilityPlanSampleStatusEnum batchSampleStatus;

    public String getPlanCreateByUserName() {
        BaseUserDO user = UserUtils.getUser(planCreateBy);
        if (ObjectUtil.isEmpty(user)) {
            return "";
        }
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }
}
