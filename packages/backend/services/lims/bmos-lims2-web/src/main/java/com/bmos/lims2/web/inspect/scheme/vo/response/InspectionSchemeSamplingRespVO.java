package com.bmos.lims2.web.inspect.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检验方案取样量配置响应VO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案取样量配置响应")
public class InspectionSchemeSamplingRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("关联的方案明细ID")
    private Long versionId;

    @ApiModelProperty("检验项目ID，为NULL时表示整体取样")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("取样量")
    private String samplingAmount;

    @ApiModelProperty("取样单位")
    private String samplingUnit;

    @ApiModelProperty("取样份数")
    private Integer samplingCount;
} 