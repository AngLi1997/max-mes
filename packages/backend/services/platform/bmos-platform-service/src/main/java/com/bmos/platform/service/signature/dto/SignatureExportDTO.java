package com.bmos.platform.service.signature.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("签名追溯日志导出DTO")
public class SignatureExportDTO {

    @ApiModelProperty("选择的id列表")
    private List<Long> selectIds;

    @ApiModelProperty("系统编码")
    private Integer systemCode;

    @ApiModelProperty("签名人")
    private String userName;

    @ApiModelProperty("开始时间")
    @NotEmpty
    private String startTime;

    @ApiModelProperty("结束时间")
    @NotEmpty
    private String endTime;



}
