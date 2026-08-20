package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 完成检验方案版本请求VO（融合基础信息 + 检验项目 + 取样配置，后台校验必填项并将版本置为已完成）
 */
@Data
@ApiModel("完成检验方案版本请求")
public class InspectionSchemeVersionCompleteReqVO {

    @ApiModelProperty(value = "基础信息", required = true)
    @NotNull(message = "基础信息不能为空")
    @Valid
    private InspectionSchemeBasicSaveReqVO basic;

    @ApiModelProperty("检验项目配置更新列表")
    @Valid
    private List<InspectionSchemeItemUpdateReqVO> itemUpdates;

    @ApiModelProperty("取样配置更新列表（schemeId/versionId 由后台从版本ID自动填充，无需前端传入）")
    private List<InspectionSchemeSamplingUpdateReqVO> samplingUpdates;
}
