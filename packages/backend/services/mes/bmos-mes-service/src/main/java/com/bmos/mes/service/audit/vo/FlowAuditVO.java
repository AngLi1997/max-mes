package com.bmos.mes.service.audit.vo;

import com.bmos.common.util.enums.EnumUtils;
import com.bmos.mes.common.enums.FlowAuditStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "审核流程配置返回Vo")
public class FlowAuditVO {

    @ApiModelProperty(value = "流程编码")
    private String code;

    @ApiModelProperty(value = "流程名称")
    private String name;

    @ApiModelProperty(value = "分类标识")
    private String categoryCode;

    @ApiModelProperty(value = "层级名称")
    private String treeName;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "状态")
    private FlowAuditStateEnum state;

    @ApiModelProperty(value = "状态名称")
    private String stateName;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "流程部署id")
    private String deploymentId;

    @ApiModelProperty(value = "模型管理表id")
    private Long id;

    @ApiModelProperty(value = "模型版本表id")
    private Long versionId;

    @ApiModelProperty(value = "引用版本")
    private String historyVersion;
}
