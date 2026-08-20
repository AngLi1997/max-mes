package com.bmos.lims2.web.inspect.scheme.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检验方案检验项目配置响应VO
 * 新的业务层级：检验方案 → 版本 → 检验项目配置 → 分析项配置
 *
 * @author yigaohui
 * @since 2025/01/21 11:00
 */
@Data
@ApiModel("检验方案检验项目配置响应")
public class InspectionSchemeItemRespVO {

    @ApiModelProperty("主键ID")
    private Long id;

    @ApiModelProperty("方案ID")
    private Long schemeId;

    @ApiModelProperty("版本ID")
    private Long versionId;

    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    @ApiModelProperty("检验项目编码")
    private String inspectItemCode;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("是否必检")
    private Boolean isRequired;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("检验项目下的分析项配置列表")
    private List<InspectionSchemeParameterRespVO> parameters;
}