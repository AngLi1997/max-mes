package com.bmos.lims2.server.eln.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 批记录-分析项绑定保存DTO
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
@Getter
@Setter
@ApiModel("批记录-分析项绑定保存DTO")
public class ParameterBindSaveDTO {

    @ApiModelProperty(value = "记录ID", required = true)
    @NotNull(message = "记录ID不能为空")
    private Long recordId;

    /**
     * 分析项ID列表，可为空；为空表示解除当前记录与分析项的绑定关系
     */
    @ApiModelProperty(value = "分析项ID列表，空则表示解除绑定", required = false)
    private List<Long> parameterIdList;
}


