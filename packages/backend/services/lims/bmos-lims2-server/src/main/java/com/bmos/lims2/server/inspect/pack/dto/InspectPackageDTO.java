package com.bmos.lims2.server.inspect.pack.dto;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 实验包(BmExperimentPackage)实体类
 *
 * @author makejava
 * @since 2024-03-02 12:46:27
 */
@Getter
@Setter
public class InspectPackageDTO extends BaseDO {

    /**
     * 实验包编码
     */
    @ApiModelProperty(value = "实验包编码")
    private String code;
    /**
     * 实验包名称
     */
    @ApiModelProperty(value = "实验包名称")
    private String name;
    /**
     * 实验包描述
     */
    @ApiModelProperty(value = "实验包描述")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String remark;

}

