package com.bmos.platform.facade.factory.vo;

import com.bmos.platform.facade.factory.enums.RoomStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 房间基础信息
 */
@Getter
@Setter
public class RoomInfoFeignVO {

    public static final String VIRTUAL_ROOM = "virtualRoom";

    /**
     * 房间id
     */
    private Long id;

    /**
     * 房间编码
     */
    private String code;

    /**
     * 房间名称
     */
    private String name;

    /**
     * 时间单位
     */
    private String timeLimit;

    /**
     * 房间状态
     * {@link com.bmos.platform.facade.factory.enums.RoomStatusEnum}
     */
    private Integer status;

    /**
     * 过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 当前房间下的工位信息
     */
    private List<FactoryStationFeignVO> stationFeignVOList;

    /**
     * 所属的数据权限
     */
    private List<Long> permisionIdList;

    public boolean judgeVirtualRoom() {
        return VIRTUAL_ROOM.equals(this.getCode());
    }

}
