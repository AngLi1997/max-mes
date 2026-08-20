package com.bmos.lims2.server.stability.review.dto;

import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 稳定性审核详情页头部信息DTO（请验信息面板）
 */
@Getter
@Setter
public class StabilityAuditHeaderDTO {

    @ApiModelProperty("检验单ID")
    private Long inspectionOrderId;

    @ApiModelProperty("稳定性考察编号")
    private String planCode;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("稳定性考察方案名称")
    private String schemeName;

    @ApiModelProperty("稳定性考察方案编码")
    private String schemeCode;

    @ApiModelProperty("稳定性考察方案版本")
    private String schemeVersion;

    @ApiModelProperty("请验人ID")
    private String requestUserId;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("请验人名称")
    public String getRequestUserName() {
        return UserUtils.getUserDisplayName(requestUserId);
    }
}
