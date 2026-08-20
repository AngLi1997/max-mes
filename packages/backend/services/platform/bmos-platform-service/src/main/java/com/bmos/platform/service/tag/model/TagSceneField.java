package com.bmos.platform.service.tag.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 标签数据源字段
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 15:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("bp_tag_scene_field")
public class TagSceneField extends BaseDO {

    /**
     * 标签场景id
     */
    @ApiModelProperty(value = "标签场景id", example = "1")
    private Long tagSceneId;

    /**
     * 字段
     */
    @ApiModelProperty(value = "字段", example = "materialNo")
    private String field;

    /**
     * 字段名称
     */
    @ApiModelProperty(value = "字段名称", example = "物料件编号")
    private String label;

    /**
     * 字段类型
     */
    @ApiModelProperty(value = "字段类型", example = "String")
    private String type;

    /**
     * 示例值
     */
    @ApiModelProperty(value = "示例值", example = "123456")
    private String exampleValue;
}
