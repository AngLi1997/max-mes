package com.bmos.lims2.server.operate.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "分类sop返回vo")
public class OperateRuleSopVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "分类名称")
    private String name;

    @ApiModelProperty(value = "上级id")
    private Long parentId;

    @ApiModelProperty(value = "是否是sop:true标识是sop")
    private Boolean flag;

    @ApiModelProperty(value = "是否删除数据")
    private Boolean disabled;

    @ApiModelProperty(value = "下级集合")
    private List<OperateRuleSopVO> children;

}
