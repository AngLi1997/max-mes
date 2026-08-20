package com.bmos.lims2.web.stability.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 新增稳定性方案（从现有方案版本复制）请求VO
 */
@Data
@ApiModel("新增稳定性方案-从现有方案版本复制 请求")
public class StabilitySchemeCopyFromVersionReqVO {

    @NotNull(message = "源方案版本ID不能为空")
    @ApiModelProperty(value = "源方案版本ID", required = true)
    private Long sourceVersionId;

    @NotBlank(message = "新方案名称不能为空")
    @ApiModelProperty(value = "新方案名称", required = true)
    private String newSchemeName;

    @NotBlank(message = "新方案编码不能为空")
    @ApiModelProperty(value = "新方案编码", required = true)
    private String newSchemeCode;

    @NotBlank(message = "新版本号不能为空")
    @ApiModelProperty(value = "新方案的首个版本号", required = true)
    private String newVersionNo;

    @Valid
    @NotNull(message = "物料信息不能为空")
    @ApiModelProperty(value = "物料信息", required = true)
    private MaterialInfoReqVO material;

    @ApiModelProperty(value = "版本描述")
    private String description;

    @ApiModelProperty(value = "数据权限-部门ID列表")
    private java.util.List<Long> deptIds;

    @Data
    @ApiModel("物料信息")
    public static class MaterialInfoReqVO {

        @NotNull(message = "物料ID不能为空")
        @ApiModelProperty(value = "物料ID", required = true)
        private Long materialId;

        @ApiModelProperty("物料名称")
        private String materialName;

        @ApiModelProperty("物料编码")
        private String materialCode;
    }
}
