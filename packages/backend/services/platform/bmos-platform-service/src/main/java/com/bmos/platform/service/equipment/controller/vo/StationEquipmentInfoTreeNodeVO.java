package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.common.tree.TreeNode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel("工位绑定设备分类树")
public class StationEquipmentInfoTreeNodeVO implements TreeNode<StationEquipmentInfoTreeNodeVO, Long, String> {

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

    /**
     * 分类类型
     */
    @ApiModelProperty("分类类型")
    private Integer type;

    @ApiModelProperty("设备信息")
    private List<EquipmentInfoStationVO> infoList;

    @ApiModelProperty("当前分类类型的孩子节点")
    private List<StationEquipmentInfoTreeNodeVO> children;

    @Override
    public String sort() {
        return code;
    }
}
