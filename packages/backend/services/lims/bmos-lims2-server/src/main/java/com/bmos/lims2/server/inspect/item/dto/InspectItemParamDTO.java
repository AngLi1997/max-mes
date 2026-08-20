package com.bmos.lims2.server.inspect.item.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class InspectItemParamDTO {

    /**
     * 检验名称
     */
    private String name;

    /**
     * 检验编码
     */
    private String code;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 不需要查询的检验项id列表集合
     */
    private List<Long> excludeIdList;

    /**
     * 分析项名称
     */
    private String parameterName;

}
