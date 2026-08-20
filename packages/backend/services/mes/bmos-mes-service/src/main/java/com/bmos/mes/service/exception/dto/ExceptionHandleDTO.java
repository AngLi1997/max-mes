package com.bmos.mes.service.exception.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@ApiModel("异常处理DTO")
@Data
public class ExceptionHandleDTO {

    @ApiModelProperty("id")
    @NotNull
    private Long id;

    @ApiModelProperty("处理人id")
    @NotBlank
    private String handleUserId;

    @ApiModelProperty("处理结果")
    @NotBlank
    private String handleResult;

    @ApiModelProperty("处理时间")
    @NotNull
    private LocalDateTime handleTime;

}
