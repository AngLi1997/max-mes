package com.bmos.platform.service.factory.controller;

import com.bmos.platform.service.factory.controller.vo.FactoryTreeNodeVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 用户具有数据权限的工位信息
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("用户具有数据权限的工位信息")
public class FactoryResourceUserVO {

    /**
     * 房间在产线中的工位
     */
    @ApiModelProperty("房间在产线中的工位")
    private List<FactoryTreeNodeVO> lineStationList;

    /**
     * 房间不在产线中的工位
     */
    @ApiModelProperty("房间不在产线中的工位")
    private List<FactoryTreeNodeVO> roomStationList;

    /**
     * 前端需要展示的所有的工位id
     */
    @ApiModelProperty("前端需要展示的所有的工位id集合")
    private List<Long> allStationIdList;

}
