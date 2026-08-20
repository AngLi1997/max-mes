package com.bmos.lims2.web.material.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 检品分类VO
 */
@Setter
@Getter
@ApiModel("保存检品分类VO")
public class MaterialCategoryCreateReqVO {

    @ApiModelProperty(value = "分类名称", required = true)
    @Length(max = 30)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "分类编码", required = true)
    @Length(max = 30)
    @NotBlank
    private String code;

    @ApiModelProperty(value = "父级Id")
    private Long parentId;

    @ApiModelProperty(value = "业务注册")
    private boolean businessRegister;

    @ApiModelProperty("业务名称")
    private String businessName;
}
