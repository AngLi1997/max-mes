package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * 工艺信息实体
 */
@Getter
@Setter
@ToString
@TableName("bm_process")
public class Process extends BaseDO {

    /**
     * 工艺名称
     */
    @NotBlank
    private String name;


    /**
     * 产品id
     */
    private Long productId;


    /**
     * 产品分类id
     */
    private Long productCategoryId;


    /**
     * 启用版本
     */
    private String activeVersion;

    @TableField(exist = false)
    @ApiModelProperty("版本号")
    private String processVersion;


}
