package com.bmos.platform.service.factory.controller.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("设备模型树")
public class StationModuleTreeNodeVO implements TreeNode<StationModuleTreeNodeVO, Long, String> {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 父节点
     */
    @ApiModelProperty("父节点")
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


    private List<StationModuleTreeNodeVO> children;

    @Override
    public String sort() {
        return code;
    }
}
