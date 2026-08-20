package com.bmos.platform.service.factory.controller.vo;

import com.bmos.platform.common.enums.factory.FactoryModuleEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 用户具有数据权限的产线信息
 */
@ApiModel("用户具有数据权限的产线/没有绑定产线的房间信息")
@Getter
@Setter
public class FactoryTreeNodeVO {

    @ApiModelProperty("产线/房间/工位id")
    private Long id;

    @ApiModelProperty("产线/房间/工位编码")
    private String code;

    @ApiModelProperty("产线/房间/工位名称")
    private String name;

    /**
     * {@link FactoryModuleEnum}
     */
    @ApiModelProperty("标识其是产线还是房间还是工位 2-产线 3-房间 4-工位")
    private Integer type;

    @ApiModelProperty("唯一标识")
    private String uniqueId;

    @ApiModelProperty("当前产线下的房间/工位信息")
    private List<FactoryTreeNodeVO> children;

}
