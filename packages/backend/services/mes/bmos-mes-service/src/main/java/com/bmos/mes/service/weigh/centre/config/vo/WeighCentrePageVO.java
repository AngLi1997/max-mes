package com.bmos.mes.service.weigh.centre.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 称量中心分页vo
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 17:37
 */
@Data
@ApiModel("称量中心分页vo")
public class WeighCentrePageVO {


    @ApiModelProperty(value = "称量中心id", example = "1")
    private Long id;

    @ApiModelProperty(value = "称量中心分类id", example = "1")
    private Long categoryId;

    @ApiModelProperty(value = "称量中心分类名称路径", example = "称量中心分类名称路径")
    private String categoryNamePath;

    @ApiModelProperty(value = "称量中心名称", example = "称量中心名称")
    private String name;

    @ApiModelProperty(value = "称量中心编码", example = "KQ-PY-101")
    private String code;

    @ApiModelProperty(value = "备注", example = "备注")
    private String remark;

    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean enabled;
}
