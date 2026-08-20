package com.bmos.lims2.server.eln.entry.vo;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.lims2.server.eln.entry.enums.ExecuteFormDataType;
import com.bmos.lims2.server.platform.util.UserUtils;
import com.bmos.mybatis.dataobject.BaseUserDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @Description: 组件异常批注VO（仅用于批注查询返回）
 * @Author: yigaohui
 * @Date: 2025/12/05 00:00
 */
@Getter
@Setter
@ToString
@ApiModel("组件异常批注VO")
public class FormDataAnnotationVO {

    @ApiModelProperty("组件id")
    private Long fieldId;

    @ApiModelProperty("批注值")
    private String value;

    @ApiModelProperty("批注值扩展")
    private String valueExtension;

    @ApiModelProperty("操作类型")
    private String operationType;

    @ApiModelProperty("操作人id")
    private String operationUser;

    @ApiModelProperty("操作人名称")
    private String operationUsername;

    @ApiModelProperty("操作人账号")
    private String operationLoginName;

    @ApiModelProperty("操作时间")
    private LocalDateTime operationTime;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("任务id")
    private Long taskId;

    public String getOperationUsername() {
        return UserUtils.getUsername(operationUser);
    }

    public String getOperationLoginName() {
        return UserUtils.getUser(operationUser) == null ? null : UserUtils.getUser(operationUser).getLoginName();
    }
}


