package com.bmos.lims2.web.stability.scheme.vo.response;

import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 稳定性方案检验项目配置响应VO
 */
@Data
@ApiModel("稳定性方案检验项目配置响应")
public class StabilitySchemeItemRespVO {

    @ApiModelProperty("检验项目配置ID")
    private Long itemConfigId;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("检验工时数量")
    private Integer duration;

    @ApiModelProperty("检验工时单位")
    private ItemDurationUnitEnum timeUnit;

    @ApiModelProperty("检验班组ID列表")
    private List<Long> teams;

    @ApiModelProperty("是否必检项")
    private Boolean isRequired;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("分析项配置列表")
    private List<StabilitySchemeParameterRespVO> inspectionParameters;
}
