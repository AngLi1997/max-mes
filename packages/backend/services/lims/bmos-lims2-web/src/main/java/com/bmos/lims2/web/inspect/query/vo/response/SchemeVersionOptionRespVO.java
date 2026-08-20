package com.bmos.lims2.web.inspect.query.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 方案版本下拉项
 * @Author: yigaohui
 * @Date: 2025/09/05 12:26
 */
@Getter
@Setter
@ApiModel("方案版本下拉项")
public class SchemeVersionOptionRespVO {
    @ApiModelProperty("版本ID")
    private Long id;
    @ApiModelProperty("版本号")
    private String versionNo;
    @ApiModelProperty("状态")
    private String status;
}


