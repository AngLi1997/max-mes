package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 新增方案-从现有方案版本复制 请求VO
 * @Author: yigaohui
 * @Date: 2025/09/03 10:35
 */
@Data
@ApiModel("新增方案-从现有方案版本复制 请求")
public class InspectionSchemeCopyFromVersionReqVO {

    @NotNull
    @ApiModelProperty(value = "源方案版本ID", required = true)
    private Long sourceVersionId;

    @NotBlank
    @ApiModelProperty(value = "新方案名称", required = true)
    private String newSchemeName;

    @NotBlank
    @ApiModelProperty(value = "新方案的首个版本号", required = true)
    private String newVersionNo;

    @ApiModelProperty("物料信息")
    @Valid
    @NotNull(message = "物料信息不能为空")
    private MaterialInfoReqVO material;

    @ApiModelProperty(value = "版本描述")
    private String description;

    /**
     * 物料信息请求VO
     */
    @Data
    @ApiModel("物料信息")
    public static class MaterialInfoReqVO {
        @ApiModelProperty(value = "物料ID", required = true)
        @NotNull(message = "物料ID不能为空")
        private Long materialId;

        @ApiModelProperty("物料名称")
        private String materialName;

        @ApiModelProperty("物料编码")
        private String materialCode;
    }
}


