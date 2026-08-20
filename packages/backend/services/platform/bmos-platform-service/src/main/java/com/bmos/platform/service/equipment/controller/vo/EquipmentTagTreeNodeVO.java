package com.bmos.platform.service.equipment.controller.vo;

import annotation.EnableBmosDataSource;
import com.bmos.common.tree.TreeNode;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author yigaohui
 * @date 2024/8/13
 **/
@NoArgsConstructor
@Getter
@Setter
public class EquipmentTagTreeNodeVO extends EquipmentTagVO implements TreeNode<EquipmentTagTreeNodeVO, Long, String> {
    private List<EquipmentTagTreeNodeVO> children;

    @Override
    public String sort() {
        return getCode();
    }
}
