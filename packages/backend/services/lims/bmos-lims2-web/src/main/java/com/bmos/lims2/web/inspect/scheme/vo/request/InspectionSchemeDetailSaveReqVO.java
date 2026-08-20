package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 检验方案明细保存请求VO
 * 正确的业务层级：检验方案明细 → 检验项目配置 → 分析项配置
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案明细保存请求")
public class InspectionSchemeDetailSaveReqVO {

    @ApiModelProperty(value = "检品ID", required = true)
    @NotNull(message = "检品ID不能为空")
    private Long materialId;

    @ApiModelProperty(value = "实验包ID", required = true)
    @NotNull(message = "实验包ID不能为空")
    private Long packageId;

    @ApiModelProperty("取样量")
    private String samplingAmount;

    @ApiModelProperty("取样单位")
    private String samplingUnit;

    @ApiModelProperty("检验项目配置列表")
    @Valid
    @NotEmpty(message = "检验项目配置列表不能为空")
    private List<InspectionSchemeItemSaveReqVO> inspectionItems;
} 