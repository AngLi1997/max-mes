package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Description: 检验方案基础信息保存请求VO
 * @Author: yigaohui
 * @Date: 2025/01/21 16:00
 */
@Data
@ApiModel("检验方案基础信息保存请求")
public class InspectionSchemeBasicSaveReqVO {



    @ApiModelProperty("方案ID（编辑时必填）")
    private Long schemeId;

    @ApiModelProperty("版本ID（编辑时必填）")
    private Long versionId;

    @ApiModelProperty(value = "方案名称", required = true)
    @NotBlank(message = "方案名称不能为空")
    private String name;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty(value = "版本号", required = true)
    @NotBlank(message = "版本号不能为空")
    private String versionNo;

    @ApiModelProperty("父版本ID（基于现有版本创建时必填）")
    private Long parentVersionId;

    @ApiModelProperty("物料信息")
    @Valid
    @NotNull(message = "物料信息不能为空")
    private MaterialInfoReqVO material;

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
