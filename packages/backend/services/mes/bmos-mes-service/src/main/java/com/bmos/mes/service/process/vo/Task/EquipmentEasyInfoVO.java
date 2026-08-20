package com.bmos.mes.service.process.vo.Task;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

/**
 * 设备简单信息
 */
@Getter
@Setter
@ApiModel(value = "设备简单信息")
public class EquipmentEasyInfoVO {

    /**
     * 主键id
     */
    private Long id;

    /**
     * 设备编码
     */
    private String code;

    /**
     * 设备名称
     */
    private String name;

}
