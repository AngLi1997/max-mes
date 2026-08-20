package com.bmos.mes.service.process.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 保存工艺看板配置dto
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 17:45
 */
@Data
@ApiModel("保存工艺看板配置dto")
public class SaveDashboardConfigProcedureDTO {

    @ApiModelProperty(value = "工序id", example = "1")
    private Long procedureId;

    @ApiModelProperty(value = "是否生效", example = "true")
    private Boolean effect;

    @ApiModelProperty(value = "自定义名称", example = "自定义名称")
    private String customName;

    @ApiModelProperty(value = "工序编码", example = "modelCode")
    private String modelCode;

    /**
     * 排序号
     */
    @ApiModelProperty(value = "排序号", example = "1")
    private Integer sort;
}
