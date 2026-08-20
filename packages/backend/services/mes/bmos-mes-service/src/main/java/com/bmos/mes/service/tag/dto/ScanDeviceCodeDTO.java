package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 扫描设备编号查询设备信息参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 13:51
 */
@Data
@ApiModel("扫描设备编号查询设备信息参数")
public class ScanDeviceCodeDTO {

    /**
     * 设备编号
     */
    @ApiModelProperty(value = "设备编号", example = "01", required = true)
    @NotBlank
    @Length(max = 100)
    private String deviceCode;

}
