package com.bmos.platform.facade.dict.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 字典数据feignVO
 */
@Getter
@Setter
public class DictDataFeignVO {

    /**
     * id
     */
    private Long id;

    /**
     * 数据标签
     */
    private String dictLabel;

    /**
     * 数据值
     */
    private String dictValue;

    /**
     * 字典id
     */
    private Long dictId;

}
