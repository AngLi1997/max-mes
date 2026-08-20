package com.bmos.platform.service.tag.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标签场景vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:14
 */
@Data
@ApiModel("标签场景")
public class TagSceneVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 标签场景名称
     */
    @ApiModelProperty(value = "标签场景名称", example = "配料称量")
    private String tagSceneName;
}
