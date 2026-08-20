package com.bmos.platform.service.tag.vo;

import com.bmos.platform.common.enums.BooleanEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 标签实例分页vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 10:14
 */
@Data
@ApiModel("标签实例分页vo")
public class TagInstancePageVO {

    /**
     * 标签实例id
     */
    @ApiModelProperty(value = "标签实例id", example = "1")
    private Long id;

    /**
     * 标签名称
     */
    @ApiModelProperty(value = "标签名称", example = "原辅包标签")
    private String tagName;

    /**
     * 标签类型名称
     */
    @ApiModelProperty(value = "标签类型名称", example = "原辅包标签")
    private String tagTypeName;

    /**
     * 标签场景名称
     */
    @ApiModelProperty(value = "标签场景名称", example = "原辅包标签")
    private String tagSceneName;

    /**
     * 起停状态
     */
    @ApiModelProperty(value = "起停状态", example = "ENABLE")
    private BooleanEnum enable;
}
