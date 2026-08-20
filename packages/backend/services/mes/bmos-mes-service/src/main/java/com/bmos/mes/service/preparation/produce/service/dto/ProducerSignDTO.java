package com.bmos.mes.service.preparation.produce.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 配液产出组件签名
 */
@Getter
@Setter
@ApiModel("配液产出组件签名DTO")
public class ProducerSignDTO {

    /**
     * 产出产出流程id
     */
    @ApiModelProperty(value = "产出产出流程id", example = "1", required = true)
    @NotNull
    private Long progressId;

    /**
     * 产出人id
     */
    @ApiModelProperty(value = "产出人id", example = "1")
    @NotNull
    private String producerId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1")
    @NotNull
    private String reCheckerId;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注")
    @Length(max = 200)
    private String remark;
    
}
