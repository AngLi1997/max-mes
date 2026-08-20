package com.bmos.lims2.server.eln.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 按分析项绑定多个记录 DTO（替代产品维度绑定）
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
@Getter
@Setter
@ApiModel("分析项维度绑定记录DTO")
public class RecordBindParameterDTO {

    @ApiModelProperty(value = "分析项ID", required = true)
    @NotNull(message = "分析项ID不能为空")
    private Long parameterId;

    @ApiModelProperty(value = "记录ID列表", required = true)
    @NotEmpty(message = "记录ID列表不能为空")
    private List<Long> recordIds;
}


