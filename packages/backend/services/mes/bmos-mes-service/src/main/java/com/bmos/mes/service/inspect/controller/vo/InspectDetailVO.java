package com.bmos.mes.service.inspect.controller.vo;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@ApiModel("请验单详情VO")
public class InspectDetailVO {

    /**
     * 检验单id
     */
    @ApiModelProperty("检验单id")
    private Long id;

    /**
     * 产品合并编码
     */
    @ApiModelProperty("产品合并编码")
    private String productMergeCode;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 指令单编号
     */
    @ApiModelProperty("指令单编号")
    private String planNo;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String batchNo;

    /**
     * 生产批量
     */
    @ApiModelProperty("生产批量")
    private BigDecimal batchQuantity;

    /**
     * 单位id
     */
    @ApiModelProperty("单位id")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String unitName;

    /**
     * 配方物料id
     */
    @ApiModelProperty("配方物料id")
    private Long formulaMaterialId;

    /**
     * 物料id
     */
    @ApiModelProperty("物料id")
    private Long materialId;

    /**
     * 物料名称
     */
    @ApiModelProperty("物料名称")
    private String materialName;

    /**
     * 物料类型
     */
    @ApiModelProperty("物料类型")
    private CategoryInfoTypeEnum materialType;

    /**
     * 请验单信息详情
     */
    @ApiModelProperty("请验单信息详情")
    private List<InspectInfoVO> inspectInfoVOList;

}
