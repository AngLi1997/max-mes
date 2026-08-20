package com.bmos.platform.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
 * 标签实例字段参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/7 16:49
 */
@Data
@ApiModel("标签实例字段参数")
public class TagInstanceField {

    /**
     * 字段名称(左侧的label)
     */
    @ApiModelProperty(value = "字段名称(左侧的label)", example = "物料名称")
    @Length(max = 10)
    private String label;

    /**
     * 定义字段(选择的模版中可配置的字段名)
     */
    @ApiModelProperty(value = "定义字段(选择的模版中可配置的字段名)", example = "materialName")
    @Length(max = 100)
    private String defineField;

    /**
     * 字段值(从数据源中获取值的字段)
     */
    @ApiModelProperty(value = "字段值(从数据源中获取值的字段)", example = "materialName")
    @Length(max = 100)
    private String dataSourceField;

    /**
     * 字段值(手动填写的值)
     */
    @ApiModelProperty(value = "字段值(手动填写的值)", example = "这里是写死的，不从数据源中获取")
    @Length(max = 100)
    private String consumeValue;
}
