package com.bmos.lims2.web.stability.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 稳定性方案基础信息保存请求VO
 */
@Data
@ApiModel("稳定性方案基础信息保存请求")
public class StabilitySchemeBasicSaveReqVO {

    @ApiModelProperty("方案ID（编辑时必填）")
    private Long schemeId;

    @ApiModelProperty("版本ID（编辑时必填）")
    private Long versionId;

    @ApiModelProperty(value = "方案名称", required = true)
    @NotBlank(message = "方案名称不能为空")
    private String name;

    @ApiModelProperty(value = "方案编码", required = true)
    @NotBlank(message = "方案编码不能为空")
    private String code;

    @ApiModelProperty(value = "版本号", required = true)
    @NotBlank(message = "版本号不能为空")
    private String versionNo;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty(value = "检品信息", required = true)
    @NotNull(message = "检品信息不能为空")
    @Valid
    private MaterialInfoReqVO material;

    @ApiModelProperty("父版本ID（复制版本时使用）")
    private Long parentVersionId;

    @ApiModelProperty("数据权限部门ID集合")
    private List<Long> deptIds;

    @Data
    @ApiModel("检品信息")
    public static class MaterialInfoReqVO {

        @ApiModelProperty(value = "检品ID", required = true)
        @NotNull(message = "检品ID不能为空")
        private Long materialId;

        @ApiModelProperty("检品名称")
        private String materialName;

        @ApiModelProperty("检品编码")
        private String materialCode;
    }
}
