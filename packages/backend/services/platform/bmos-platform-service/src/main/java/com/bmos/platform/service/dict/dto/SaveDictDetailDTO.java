package com.bmos.platform.service.dict.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "添加字典数据dto")
public class SaveDictDetailDTO {

    @ApiModelProperty(value = "数据标签")
    @NotBlank
    @Length(max = 100)
    private String dictLabel;

    @ApiModelProperty(value = "数据值")
    @NotBlank
    @Length(max = 100)
    private String dictValue;

    @ApiModelProperty(value = "字典表id")
    @NotNull
    private Long dictId;
}
