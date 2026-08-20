package com.bmos.lims2.server.report.dto;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.ReportOperationTypeEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("报告模板版本操作历史DTO")
public class ReportTemplateOperationHistoryDTO {
    @ApiModelProperty("ID")
    private Long id;
    @ApiModelProperty("模板版本ID")
    private Long templateVersionId;
    @ApiModelProperty("操作类型")
    private ReportOperationTypeEnum operationType;
    @ApiModelProperty("操作人ID")
    private String operatorId;
    @ApiModelProperty("操作人姓名")
    private String operatorName;
    @ApiModelProperty("操作时间")
    private LocalDateTime operateTime;
    @ApiModelProperty("备注")
    private String remark;
    private String createBy;
    private String createUsername;
    private LocalDateTime createTime;

    public String getCreateUsername() {
        BaseUserDO user = UserUtils.getUser(createBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }
}

