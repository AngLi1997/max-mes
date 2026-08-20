package com.bmos.mes.service.process.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;

@ApiModel("工位VO")
@Data
public class ProcedureModelRoomOrStationVO {

    private Long id;

    /**
     * code，用于唯一标识工位
     */
    private String code;
    /**
     * 名称，对工位的描述性文字
     */
    private String name;

    /**
     * 是否为房间标识
     */
    private boolean roomFlag;

    /**
     * 展示名称
     */
    private String showName;

    /**
     * 若此有值 代表此为当前为房间信息
     */
    private List<ProcedureModelRoomOrStationVO> children;

    /**
     * 工位标识
     */
    private boolean stationFlag;

    private String roomIdPath;
}
