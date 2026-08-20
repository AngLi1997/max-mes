package com.bmos.platform.facade.factory.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 产线被工艺配置绑定
 */
@Getter
@Setter
public class LineUseDTO {

    /**
     * 产线被工艺配置绑定
     */
    private Map<Long, Boolean> lineUseMap;

    /**
     * 房间被工艺配置绑定
     */
    private Map<Long, Boolean> roomUseMap;

    /**
     * 工位被工艺配置绑定
     */
    private Map<Long, Boolean> stationUseMap;

}
