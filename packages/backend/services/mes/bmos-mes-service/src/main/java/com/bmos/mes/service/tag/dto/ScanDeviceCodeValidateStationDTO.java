package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 扫描设备编号查询设备信息校验工位参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 13:51
 */
@Data
@ApiModel("扫描设备编号查询设备信息校验工位参数")
public class ScanDeviceCodeValidateStationDTO {

    @ApiModelProperty(value = "设备编号", example = "01", required = true)
    @NotBlank
    @Length(max = 100)
    private String deviceCode;

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long productPlanId;

    @ApiModelProperty(value = "工序步骤模型id", example = "1")
    private Long procedureStepModelId;

    @ApiModelProperty(value = "组件id", example = "1")
    private Long componentId;

}
