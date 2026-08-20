package com.bmos.lims2.web.material.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 检品详情信息VO
 */
@Getter
@Setter
@ApiModel("检品详情信息")
public class MaterialInfoRespVO {

    @ApiModelProperty("检品ID")
    private Long id;

    @ApiModelProperty("检品名称")
    private String name;

    @ApiModelProperty("检品编码")
    private String mergeCode;

    @ApiModelProperty("规格")
    private String specification;

    @ApiModelProperty("单位")
    private String unit;


    @ApiModelProperty(value = "检品单位扩展id")
    private Long extendUnitId;

    @ApiModelProperty("描述")
    private String description;

    /**
     * 检品备注
     */
    @ApiModelProperty("检品备注")
    private String remark;

    @ApiModelProperty("自定义信息")
    private List<MaterialFieldVO> fieldList;

}
