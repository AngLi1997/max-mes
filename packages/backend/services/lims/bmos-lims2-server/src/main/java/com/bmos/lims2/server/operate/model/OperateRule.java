package com.bmos.lims2.server.operate.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@TableName(value = "bm_operate_rule")
public class OperateRule extends BaseDO {

    @ApiModelProperty("文件名称")
    private String name;

    @ApiModelProperty("文件编号")
    private String code;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("版本号")
    @TableField(exist = false)
    private String version;

    @ApiModelProperty("版本id")
    @TableField(exist = false)
    private Long versionId;

    @ApiModelProperty("文件地址")
    @TableField(exist = false)
    private String url;
}
