package com.bmos.mes.service.facotry.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("房间VO")
public class FactoryStationVO {

    /**
     * 工位id
     */
    private Long id;

    /**
     * 工位编码
     */
    private String code;

    /**
     * 工位名称
     */
    private String name;

}
