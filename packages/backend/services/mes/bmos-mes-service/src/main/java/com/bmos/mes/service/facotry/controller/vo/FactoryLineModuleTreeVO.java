package com.bmos.mes.service.facotry.controller.vo;

import com.bmos.common.tree.TreeNode;
import com.bmos.platform.facade.factory.vo.FactoryLineFeignVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("产线树结构")
public class FactoryLineModuleTreeVO implements TreeNode<FactoryLineModuleTreeVO, Long, String> {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 父级id
     */
    @ApiModelProperty("父级id")
    private Long parentId;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String name;
    /**
     * 编码
     */
    @ApiModelProperty("编码")
    private String code;

    /**
     * 前端显示名称
     */
    @ApiModelProperty("前端显示名称")
    private String showName;

    /**
     * 是否为产线
     */
    private Boolean lineFlag;

    /**
     * 当前产线是否被删除
     */
    private Boolean disabled;

    /**
     * 孩子节点
     */
    @ApiModelProperty("孩子节点(包含产线信息)")
    private List<FactoryLineModuleTreeVO> children;

    @Override
    public String sort() {
        return code;
    }
}
