package com.bmos.platform.facade.equipment.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "设备模块树")
public class EquipmentModuleTreeNodeFeignVO implements TreeNode<EquipmentModuleTreeNodeFeignVO, Long, String> {

    /**
     * 模块id
     */
    private Long id;

    /**
     * 父模块id
     */
    private Long parentId;

    /**
     * 模块名称
     */
    private String name;

    /**
     * 模块编码
     */
    private String code;

    /**
     * 子模块
     */
    private List<EquipmentModuleTreeNodeFeignVO> children;

    /**
     * 设备信息
     */
    private List<EquipmentEasyInfoFeignVO> infoList;

    @Override
    public String sort() {
        return this.code;
    }
}
