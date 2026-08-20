package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName BusinessParameterVO
 * @Description 时间差格式化返回vo
 * @Author Ren Jin Guang
 * @Date 2024/10/31 15:47
 */
@Setter
@Getter
@ToString
public class BusinessParameterVO {

    @ApiModelProperty("label")
    private String label;

    @ApiModelProperty("值")
    private String value;
}
