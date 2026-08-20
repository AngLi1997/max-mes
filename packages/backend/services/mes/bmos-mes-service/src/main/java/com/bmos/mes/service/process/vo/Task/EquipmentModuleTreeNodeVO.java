package com.bmos.mes.service.process.vo.Task;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel(description = "设备模块树")
public class EquipmentModuleTreeNodeVO implements TreeNode<EquipmentModuleTreeNodeVO, Long, String> {

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
     * 是否是设备。true是
     */
    private Boolean flag;

    /**
     * 是否为删除数据
     */
    private Boolean disabled;

    /**
     * 子模块
     */
    private List<EquipmentModuleTreeNodeVO> children;

    /**
     * 设备信息
     */
    private List<EquipmentEasyInfoVO> infoList;

    @Override
    public String sort() {
        return this.code;
    }
}
