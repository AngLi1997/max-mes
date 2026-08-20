package com.bmos.wms.service.inspect.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 请验单配置详情（用于前端选择请验单 + 渲染字段表单）。
 *
 * <p>与 mes 同款 InspectConfigDetailVO；走自研 LIMS 路径时由 BmosLimsGateway 从 LIMS 转出。
 */
@Getter
@Setter
@ApiModel("请验单配置详情")
public class InspectConfigDetailVO {

    @ApiModelProperty("请验单id（LIMS document_config.id）")
    private Long id;

    @ApiModelProperty("请验单名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("请验单字段列表")
    private List<InspectConfigDataVO> dataList;
}
