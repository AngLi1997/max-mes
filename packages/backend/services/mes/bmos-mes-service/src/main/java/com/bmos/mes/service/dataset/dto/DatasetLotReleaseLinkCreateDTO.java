package com.bmos.mes.service.dataset.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 批签发引用创建DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:48
 */
@Data
@ApiModel("批签发引用创建DTO")
public class DatasetLotReleaseLinkCreateDTO {

    @ApiModelProperty(value = "批签发模版id", example = "1")
    @NotNull
    private Long lotReleaseTemplateId;

    @ApiModelProperty(value = "批签发模板版本", example = "V1")
    @NotBlank
    @Length(max = 100)
    private String lotReleaseVersion;

    @ApiModelProperty(value = "批签发名称", example = "批签发名称")
    @NotBlank
    @Length(max = 100)
    private String lotReleaseName;

    @ApiModelProperty(value = "批签发引用参数范围", example = "P15:S19")
    @NotBlank
    @Length(max = 100)
    private String linkArea;
}
