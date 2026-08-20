package com.bmos.lims2.server.inspect.item.dto;

import com.bmos.lims2.server.inspect.parameter.dto.InspectParameterDataPointDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @className: InspectItemParameterDTO
 * @author: yigaohui
 * @date: 2025/7/17 15:07
 * @Version: 1.0
 * @description:
 */
@Setter
@Getter
@ApiModel("检验项目中的分析项DTO")
public class InspectItemParameterDTO {


    @ApiModelProperty(value = "分析项id", required = true)
    @NotNull
    private Long inspectParameterId;

    @ApiModelProperty(value = "分析项编码")
    private String code;

    @ApiModelProperty(value = "分析项名称")
    private String name;

    @ApiModelProperty(value = "标准规定")
    private String standard;

    @ApiModelProperty(value = "数据点列表")
    private List<InspectParameterDataPointDTO> dataPoints;
}
