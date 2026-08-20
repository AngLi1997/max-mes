package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 检验方案检验项目配置保存请求VO
 * 正确的业务层级：检验方案明细 → 检验项目配置 → 分析项配置
 *
 * @author yigaohui
 * @since 2025/01/21 11:00
 */
@Data
@ApiModel("检验方案检验项目配置保存请求")
public class InspectionSchemeItemSaveReqVO {

    @ApiModelProperty(value = "检验项目ID", required = true)
    @NotNull(message = "检验项目ID不能为空")
    private Long inspectItemId;

    @ApiModelProperty("是否必检")
    private Boolean isRequired = true;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("检验项目下的分析项配置列表")
    @Valid
    private List<InspectionSchemeParameterSaveReqVO> parameters;
}