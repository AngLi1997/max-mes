package com.bmos.lims2.server.inspect.scheme.dto.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检验方案下拉数据DTO
 */
@Data
@ApiModel("检验方案下拉数据DTO")
public class InspectionSchemeDropdownDTO {

    @ApiModelProperty("检验方案ID")
    private Long id;

    @ApiModelProperty("检验方案名称")
    private String name;

    @ApiModelProperty("方案编码")
    private String code;

    @ApiModelProperty("当前生效版本号")
    private String activeVersionNo;

    @ApiModelProperty("当前生效版本ID")
    private Long activeVersionId;

    @ApiModelProperty("物料ID")
    private Long materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("实验包ID")
    private Long packageId;

    @ApiModelProperty("实验包编码")
    private String packageCode;

    @ApiModelProperty("实验包名称")
    private String packageName;

    @ApiModelProperty("显示名称（方案名称 - 版本号）")
    private String displayName;
}
