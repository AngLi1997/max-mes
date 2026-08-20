package com.bmos.mes.service.trace.material.entity;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/11/20 13:42
 */
@Data
public class MaterialTraceTemplateMaterial {

    @ApiModelProperty(value = "物料关联id", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelEnumProperty(value = "物料类型", enumClass = CategoryInfoTypeEnum.class)
    private Integer materialType;

    @ApiModelProperty(value = "是否显示收率", example = "true")
    private Boolean showPercentYield;

    @ApiModelProperty(value = "收率范围")
    private PercentYieldRange percentYieldRange;

    @ApiModelProperty(value = "是否参与计算", example = "true")
    private Boolean calcFlag;

    @ApiModelProperty(value = "子物料列表")
    private List<MaterialTraceTemplateMaterial> children;

    @ApiModelProperty("父级id(仅获取详情时需要，传参可忽略)")
    private Long parentId;
}
