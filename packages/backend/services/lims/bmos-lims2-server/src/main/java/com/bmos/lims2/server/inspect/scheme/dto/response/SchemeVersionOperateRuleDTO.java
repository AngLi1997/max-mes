package com.bmos.lims2.server.inspect.scheme.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 方案版本关联操作规程 DTO
 */
@Data
@ApiModel("方案版本关联操作规程")
public class SchemeVersionOperateRuleDTO {

    @ApiModelProperty("操作规程ID")
    private Long operateId;

    @ApiModelProperty("操作规程名称")
    private String name;

    @ApiModelProperty("操作规程编码")
    private String code;

    @ApiModelProperty("操作规程版本ID")
    private Long versionId;

    @ApiModelProperty("操作规程版本号")
    private String versionNo;
}
