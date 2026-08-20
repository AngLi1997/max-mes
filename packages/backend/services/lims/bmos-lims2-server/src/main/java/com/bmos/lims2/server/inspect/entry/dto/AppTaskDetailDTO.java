package com.bmos.lims2.server.inspect.entry.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @Description: APP 任务详情 DTO（在任务子项基础上补充审核人信息及名称-编码格式字段）
 * @Author: yigaohui
 */
@Getter
@Setter
@ApiModel("APP-任务详情")
public class AppTaskDetailDTO extends AppTaskEntryItemDTO {

    @ApiModelProperty("审核人ID")
    private String sampleAuditBy;

    @ApiModelProperty("审核时间")
    private LocalDateTime sampleAuditTime;

    @ApiModelProperty("实验所有人 名称-编码")
    private String ownerNameCode;

    @ApiModelProperty("复核人 名称-编码")
    private String reviewerNameCode;

    @ApiModelProperty("审核人 名称-编码")
    private String sampleAuditorNameCode;

    /** 实验所有人 名称-编码（格式：userName-loginName） */
    public String getOwnerNameCode() {
        if (getOwnerId() == null) return null;
        BaseUserDO user = UserUtils.getUser(getOwnerId().toString());
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    /** 复核人 名称-编码（格式：userName-loginName） */
    public String getReviewerNameCode() {
        BaseUserDO user = UserUtils.getUser(getReviewedBy());
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    /** 审核人 名称-编码（格式：userName-loginName） */
    public String getSampleAuditorNameCode() {
        BaseUserDO user = UserUtils.getUser(sampleAuditBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}
