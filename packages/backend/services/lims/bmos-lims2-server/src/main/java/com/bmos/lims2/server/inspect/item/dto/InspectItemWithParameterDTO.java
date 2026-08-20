package com.bmos.lims2.server.inspect.item.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @className: InspectItemWithParameterDTO
 * @author: yigaohui
 * @date: 2025/7/17 15:11
 * @Version: 1.0
 * @description:
 */

@Setter
@Getter
@ApiModel("检验项目-带上分析项DTO")
public class InspectItemWithParameterDTO extends InspectItemDTO {
    /**
     * 当前检验项下的分析项
     */
    @ApiModelProperty(value = "当前检验项下的分析项", required = true)
    @NotNull
    private List<InspectItemParameterDTO> parameterList;
}
