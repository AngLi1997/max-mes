package com.bmos.lims2.server.inspect.pack.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 实验包查询参数
 */
@Getter
@Setter
public class PackageParamDTO {

    /**
     * 实验包编码
     */
    private String code;

    /**
     * 实验包名称
     */
    private String name;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 排除的检验项目id
     */
    private List<Long> excludeIdList;

    /**
     * 检验项目名称 - 用于筛选
     */
    @ApiModelProperty(value = "检验项目名称 - 用于筛选")
    private String inspectItemName;

}
