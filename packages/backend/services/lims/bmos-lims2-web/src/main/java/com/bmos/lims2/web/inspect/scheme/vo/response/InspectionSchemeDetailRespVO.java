package com.bmos.lims2.web.inspect.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验方案明细响应VO
 * 正确的业务层级：检验方案明细 → 检验项目配置 → 分析项配置
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
@ApiModel("检验方案明细响应")
public class InspectionSchemeDetailRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("关联的版本ID")
    private Long versionId;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("实验包ID")
    private Long packageId;

    @ApiModelProperty("实验包名称")
    private String packageName;

    @ApiModelProperty("实验包编码")
    private String packageCode;

    @ApiModelProperty("取样量")
    private String samplingAmount;

    @ApiModelProperty("取样单位")
    private String samplingUnit;

    @ApiModelProperty("检验项目配置列表")
    private List<InspectionSchemeItemRespVO> inspectionItems;
} 