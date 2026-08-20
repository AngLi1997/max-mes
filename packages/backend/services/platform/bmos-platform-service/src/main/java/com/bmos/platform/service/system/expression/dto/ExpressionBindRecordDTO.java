package com.bmos.platform.service.system.expression.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("公式绑定记录DTO")
public class ExpressionBindRecordDTO {

    @ApiModelProperty("公式id")
    @NotNull
    private Long id;

    @ApiModelProperty("记录id列表")
    private List<Long> recordIdList;

}
