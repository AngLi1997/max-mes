package com.bmos.lims2.server.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "新建流程保存dto")
public class SaveAuditDTO {

    @ApiModelProperty(value = "流程定义id")
    private String deploymentId;

    @ApiModelProperty(value = "流程id")
    private Long flowAuditId;

    @ApiModelProperty(value = "版本id")
    private Long versionId;

    @ApiModelProperty(value = "流程编码")
    private String code;

    @ApiModelProperty(value = "流程名称")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "分类标识")
    @NotBlank
    private String categoryCode;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "版本号")
    @NotBlank
    private String version;

    @ApiModelProperty(value = "流程模型")
    @NotBlank
    private String flowAuditModel;

    @ApiModelProperty(value = "是否增加版本")
    private Boolean changeVersion;

    @ApiModelProperty(value = "审核人员集合")
    private List<SaveFlowAuditUserDTO> auditUserList;

    @ApiModelProperty(value = "消息通知人员集合")
    private List<SaveFlowAuditMegDTO> auditMegDTOList;

    @ApiModelProperty("源版本")
    private String sourceVersion;
}
