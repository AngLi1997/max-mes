package com.bmos.mes.service.facotry.service.data;

import com.bmos.mes.common.model.component.BasicComponentConfig;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 清场组件的功能属性
 */
@Getter
@Setter
public class CleanRoomComponentConfig extends BasicComponentConfig {

    /**
     * 房间id集合
     */
    private List<Long> roomIdList;

    /**
     * 房间id集合（格式：产线id房间id）
     */
    private List<String> roomIdListShow;

}
