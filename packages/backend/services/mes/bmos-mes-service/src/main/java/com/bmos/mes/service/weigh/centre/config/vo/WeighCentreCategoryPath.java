package com.bmos.mes.service.weigh.centre.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 称量中心分类路径vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/6/7 10:21
 */
@Data
@ApiModel("称量中心分类路径vo")
public class WeighCentreCategoryPath {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", example = "称量中心分类名称", required = true)
    private String name;

    /**
     * 分类名称路径
     */
    @ApiModelProperty(value = "分类id路径", example = "称量中心分类id路径")
    private String idPath;

    /**
     * 分类名称路径
     */
    @ApiModelProperty(value = "分类名称路径", example = "称量中心分类名称路径")
    private String namePath;
}
