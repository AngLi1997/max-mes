package com.bmos.lims2.server.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@TableName(value = "lm_flow_audit_version")
public class FlowAuditVersion extends BaseDO {

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "状态，1：编辑中；2：启用中；3：历史")
    private Integer state;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "模型管理表id")
    private Long flowAuditId;

    @ApiModelProperty(value = "流程部署id")
    private String deploymentId;

    @ApiModelProperty(value = "引用版本号")
    private String historyVersion;
}
