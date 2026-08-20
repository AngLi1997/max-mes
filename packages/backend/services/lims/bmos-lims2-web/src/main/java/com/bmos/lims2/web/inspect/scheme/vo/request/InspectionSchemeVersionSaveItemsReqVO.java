package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 保存方案版本检验项目-分析项请求VO
 * 前端直接选择分析项传入，后台按检验项目去重分组保存
 */
@Data
@ApiModel("保存方案版本检验项目-分析项请求")
public class InspectionSchemeVersionSaveItemsReqVO {

    @ApiModelProperty(value = "方案ID", required = true)
    @NotNull(message = "方案ID不能为空")
    private Long schemeId;

    @ApiModelProperty(value = "版本ID", required = true)
    @NotNull(message = "版本ID不能为空")
    private Long versionId;

    @ApiModelProperty(value = "分析项列表（前端选择后传入，后台按检验项目去重分组）", required = true)
    @NotEmpty(message = "分析项列表不能为空")
    @Valid
    private List<ParameterItemVO> parameters;

    @Data
    @ApiModel("分析项选择项")
    public static class ParameterItemVO {

        @ApiModelProperty(value = "检验项目ID", required = true)
        @NotNull(message = "检验项目ID不能为空")
        private Long inspectItemId;

        @ApiModelProperty(value = "分析项ID", required = true)
        @NotNull(message = "分析项ID不能为空")
        private Long parameterId;
    }
}
