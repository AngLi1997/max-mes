package com.bmos.lims2.web.inspect.query.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 方案下拉项
 * @Author: yigaohui
 * @Date: 2025/09/05 12:25
 */
@Getter
@Setter
@ApiModel("方案下拉项")
public class SchemeOptionRespVO {
    @ApiModelProperty("方案ID")
    private Long id;
    @ApiModelProperty("方案名称")
    private String name;
}


