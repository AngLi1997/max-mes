package com.bmos.lims2.server.inspect.item.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @className: InspectItemDTO
 * @author: yigaohui
 * @date: 2025/7/17 15:07
 * @Version: 1.0
 * @description:
 */
@Setter
@Getter
@ApiModel("检验项目带上分析项DTO")
public class InspectItemDTO extends BaseDO {

    /**
     * 检验项目编码
     */
    @ApiModelProperty(value = "检验项目编码")
    private String code;
    /**
     * 检验项目名称
     */
    @ApiModelProperty(value = "检验项目名称")
    private String name;
    /**
     * 检验项目描述
     */
    @ApiModelProperty(value = "检验项目描述")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /**
     * 是否为系统内置项目（0-否，1-是）
     * 系统内置项目不在前端列表中显示
     */
    @ApiModelProperty(value = "是否为系统内置项目")
    private Boolean isSystem;
}
