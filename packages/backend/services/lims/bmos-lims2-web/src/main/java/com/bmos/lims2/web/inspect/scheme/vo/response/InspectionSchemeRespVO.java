package com.bmos.lims2.web.inspect.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检验方案响应VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案响应")
public class InspectionSchemeRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("方案编码")
    private String code;

    @ApiModelProperty("方案名称")
    private String name;

    @ApiModelProperty("方案描述")
    private String description;

    @ApiModelProperty("当前生效版本号")
    private String activeVersionNo;

    @ApiModelProperty("当前生效版本ID")
    private Long activeVersionId;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("创建人ID")
    private Long createBy;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("物料ID")
    private Long materialId;

    @ApiModelProperty("物料编码")
    private String materialCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("单位id")
    private Long materialUnitId;

    @ApiModelProperty("单位名称")
    private String materialUnitName;

    @ApiModelProperty("实验包ID")
    private Long packageId;

    @ApiModelProperty("实验包编码")
    private String packageCode;

    @ApiModelProperty("实验包名称")
    private String packageName;
} 