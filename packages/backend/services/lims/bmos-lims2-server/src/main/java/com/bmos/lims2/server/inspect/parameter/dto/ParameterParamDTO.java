package com.bmos.lims2.server.inspect.parameter.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分析项sql查询参数
 */
@Getter
@Setter
public class ParameterParamDTO {

    /**
     * 分析项编码
     */
    private String code;

    /**
     * 分析项名称
     */
    private String name;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 需要排除的id列表集合
     */
    private List<Long> excludeIdList;

}
