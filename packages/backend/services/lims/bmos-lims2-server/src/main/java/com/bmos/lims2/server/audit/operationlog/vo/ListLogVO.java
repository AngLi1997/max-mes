package com.bmos.lims2.server.audit.operationlog.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.common.enums.OperationType;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "批记录版本VO")
public class ListLogVO {

    @ApiModelProperty("操作类型")
    private OperationType operationType;

    @ApiModelProperty("操作类型名称")
    private String operationTypeName;

    @ApiModelProperty("操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty("操作人")
    private String createBy;

    @ApiModelProperty("操作人名称")
    private String createByName;

    private String createUsername;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("节点名称")
    private String nodeName;

    @ApiModelProperty("操作")
    private String comment;

    @ApiModelProperty("详情")
    private String detail;

    public String getCreateUsername() {
        BaseUserDO user = UserUtils.getUser(createBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

    public String getCreateByName() {
        BaseUserDO user = UserUtils.getUser(createBy);
        return ObjectUtil.isNotEmpty(user) ? user.getUserName() + StrUtil.DASHED + user.getLoginName() : null;
    }

}
