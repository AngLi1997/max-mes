package com.bmos.lims2.web.inspect.scheme.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 检验方案保存融合请求VO（基础信息+项目配置更新+取样配置更新）
 * @Author: yigaohui
 * @Date: 2025/10/24 10:00
 */
@Data
@ApiModel("检验方案保存融合请求")
public class InspectionSchemeSaveFusionReqVO {

    @ApiModelProperty(value = "基础信息", required = true)
    @Valid
    @NotNull(message = "基础信息不能为空")
    private InspectionSchemeBasicSaveReqVO basic;

    @ApiModelProperty("检验项目配置更新列表（等价于 /inspection-scheme/update-items 的入参）")
    @Valid
    private List<InspectionSchemeItemUpdateReqVO> itemUpdates;

    @ApiModelProperty("取样配置更新列表（等价于 /inspection-scheme/update-samplings 的入参）")
    @Valid
    private List<InspectionSchemeSamplingUpdateReqVO> samplingUpdates;
}


