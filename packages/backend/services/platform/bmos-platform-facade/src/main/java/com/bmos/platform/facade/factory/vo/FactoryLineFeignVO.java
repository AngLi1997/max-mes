package com.bmos.platform.facade.factory.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 产线FeignVO
 */
@Getter
@Setter
public class FactoryLineFeignVO {

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
     * 模型id
     */
    private Long moduleId;

    /**
     * 是否启动
     */
    private Boolean enable;

    /**
     * 删除状态
     */
    private Boolean isDeleted;

}
