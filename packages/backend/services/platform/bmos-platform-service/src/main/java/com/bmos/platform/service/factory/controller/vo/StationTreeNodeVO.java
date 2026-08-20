package com.bmos.platform.service.factory.controller.vo;

import com.bmos.common.tree.TreeNode;
import com.bmos.platform.service.equipment.controller.vo.EquipmentInfoStationVO;
import com.bmos.platform.service.equipment.controller.vo.StationEquipmentInfoTreeNodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@ApiModel("工位树VO")
public class StationTreeNodeVO implements TreeNode<StationTreeNodeVO, Long, String> {

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
     * 工位信息
     */
    @ApiModelProperty("工位信息")
    private List<StationEasyVO> infoList;

    @ApiModelProperty("当前分类类型的孩子节点")
    private List<StationTreeNodeVO> children;

    @Override
    public String sort() {
        return code;
    }

}
