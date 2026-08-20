package com.bmos.mes.common.model.component;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 组件的基础功能属性
 */
@Getter
@Setter
public class BasicComponentConfig {

    /**
     * 工位id集合
     */
    private List<Long> station;

    /**
     * 工位与产线绑定关系
     */
    private List<String> stationShow;


}
