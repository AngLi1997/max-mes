package com.bmos.lims2.server.eln.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("记录绑定公式DTO")
@Data
public class RecordBindExpressionDTO {

    @ApiModelProperty("记录id")
    @NotNull
    private Long id;

    @ApiModelProperty("公式id列表")
    private List<Long> expressionIdList;

}
