package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 请验单配置保存请求参数
 */
@Getter
@Setter
@ApiModel("请验单配置保存请求参数")
public class InspectConfigSaveDTO {

    @ApiModelProperty("请验单名称")
    private String name;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;

    /**
     * 请验单数据
     */
    @ApiModelProperty("请验单数据")
    private List<InspectConfigDataSaveDTO> dataList;

}
