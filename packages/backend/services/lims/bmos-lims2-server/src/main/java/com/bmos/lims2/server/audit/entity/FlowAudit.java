package com.bmos.lims2.server.audit.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName(value = "lm_flow_audit")
public class FlowAudit extends BaseDO {

    @ApiModelProperty(value = "流程编码")
    private String code;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "分类标识")
    private String categoryCode;
}
