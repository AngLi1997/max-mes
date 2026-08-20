package com.bmos.lims2.web.stability.plan.vo.response;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.StabilityInspectPlanStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 稳定性考察计划列表响应VO
 */
@Data
@ApiModel("稳定性考察计划列表响应")
public class StabilityInspectPlanRespVO {

    @ApiModelProperty("计划ID")
    private Long id;

    @ApiModelProperty("考察计划编号")
    private String code;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("稳定性方案名称")
    private String schemeName;

    @ApiModelProperty("稳定性方案版本号")
    private String schemeVersionNo;

    @ApiModelProperty("计划状态（PENDING/IN_PROGRESS/COMPLETED/PAUSED）")
    private StabilityInspectPlanStatusEnum status;

    @ApiModelProperty("开始时间")
    private LocalDate startDate;

    @ApiModelProperty("计划结束时间")
    private LocalDate planEndDate;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("试验类型（多个用逗号隔开）")
    private String experimentTypeNames;

    @ApiModelProperty("创建人名称（格式：姓名-登录名）")
    private String createByUserName;


    public String getCreateByUserName() {
        BaseUserDO user = UserUtils.getUser(createBy);
        if (ObjectUtil.isEmpty(user)){
            return "";
        }
        return user.getUserName() + StrUtil.DASHED + user.getLoginName();
    }
}
