package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

/**
 * 发起请验信息的DTO
 */
@Setter
@Getter
@ApiModel(value = "重新发起请验时修改的信息DTO")
public class InitiateRetryInspectInfoDTO {

    /**
     * 请验单信息id
     */
    @ApiModelProperty("请验单信息id")
    private Long id;

    /**
     * 修改后的值
     */
    @ApiModelProperty("值")
    private String value;

}
