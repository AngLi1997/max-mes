package com.bmos.platform.service.equipment.controller.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yigaohui
 * @date 2024/4/25
 **/
@Data
public class AcquisitionPointExportVO extends AcquisitionPointPageQueryVO{
    /**
     * 是否导出全部
     */
    @ApiModelProperty(value = "是否导出全部",required = true)
    private Boolean all;
}
