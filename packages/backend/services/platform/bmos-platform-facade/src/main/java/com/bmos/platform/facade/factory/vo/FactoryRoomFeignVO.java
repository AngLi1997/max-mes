package com.bmos.platform.facade.factory.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 产线FeignVO
 */
@Getter
@Setter
public class FactoryRoomFeignVO {

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
     * 模型id
     */
    private Long moduleId;

    /**
     * 是否启动
     */
    private Boolean enable;

    /**
     * 是否删除
     */
    private Boolean isDeleted;

}
