package com.bmos.mes.service.formula.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.audit.engine.core.query.resp.TaskListResp;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel("产品配方审核分页VO")
public class ProductFormulaAuditPageVO extends TaskListResp {

    @ApiModelProperty("配方版本id")
    private Long id;

    @ApiModelProperty("产品名称")
    private String productName;

    @ApiModelProperty("产品编码")
    private String productMergeCode;

    @ApiModelProperty("配方名称")
    private String name;

    @ApiModelProperty("配方版本号")
    private String versionNo;

    @ApiModelProperty("配方描述")
    private String description;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("流程发起人")
    private String processStartBy;

    @ApiModelProperty("开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("流程发起人")
    private String processStartUser;

    public String getProcessStartUser() {
        BaseUserDO user = UserUtils.getUser(processStartBy);
        return ObjectUtil.isNotEmpty(user) ? (user.getUserName() + StrUtil.DASHED + user.getLoginName()) : "";
    }

}
