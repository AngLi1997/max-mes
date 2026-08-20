package com.bmos.platform.facade.factory.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 产线FeignVO
 */
@Getter
@Setter
public class FactoryLineDetailFeignVO {

    /**
     * 产线id
     */
    private Long id;

    /**
     * 产线编码
     */
    private String code;

    /**
     * 产线名称
     */
    private String name;

    /**
     * 房间信息
     */
    private List<RoomInfoFeignVO> roomInfoFeignVOList;

    /**
     * 产线与工位的直接绑定
     */
    private List<FactoryStationFeignVO> stationFeignVOList;

}
