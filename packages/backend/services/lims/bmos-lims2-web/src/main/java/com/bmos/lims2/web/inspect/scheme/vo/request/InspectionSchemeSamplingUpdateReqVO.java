package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 检验方案取样配置编辑保存请求VO
 * @Author: yigaohui
 * @Date: 2025/01/21 17:05
 */
@Data
@ApiModel("检验方案取样配置编辑保存请求")
public class InspectionSchemeSamplingUpdateReqVO {

    @ApiModelProperty(value = "方案ID", required = true)
    @NotNull(message = "方案ID不能为空")
    private Long schemeId;

    @ApiModelProperty(value = "版本ID", required = true)
    @NotNull(message = "版本ID不能为空")
    private Long versionId;

    @ApiModelProperty("取样配置ID（修改时需要）")
    private Long samplingConfigId;

    @ApiModelProperty(value = "检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("取样数量")
    private String samplingAmount;

    @ApiModelProperty("取样单位")
    private String samplingUnit;

    @ApiModelProperty("取样次数")
    private Integer samplingCount;
}
