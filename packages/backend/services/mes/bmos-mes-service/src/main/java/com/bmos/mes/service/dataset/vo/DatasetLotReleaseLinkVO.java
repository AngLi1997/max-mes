package com.bmos.mes.service.dataset.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 批签发引用vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:48
 */
@Data
@ApiModel("批签发引用vo")
public class DatasetLotReleaseLinkVO {

    @ApiModelProperty(value = "批签发引用id", example = "1")
    private Long id;

    @ApiModelProperty(value = "批签发引用名称", example = "批签发引用名称")
    private String name;

    @ApiModelProperty(value = "批签发id", example = "1")
    private Long lotReleaseTemplateId;

    @ApiModelProperty(value = "批签发版本", example = "V1")
    private String lotReleaseVersion;

    @ApiModelProperty(value = "批签发引用参数范围", example = "P15:S19")
    private String linkArea;

    @ApiModelProperty(value = "模板url", example = "http://www.baidu.com")
    private String templateUrl;
}
