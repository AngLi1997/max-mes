package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 13:51
 */
@Data
@ApiModel("扫描物料件号查询物料件信息参数（校验配料单信息）")
public class ScanWeighMaterialCodeWithMaterialWeighComponentId {

    /**
     * 物料件号/容器编号(优先查询物料件号)
     */
    @ApiModelProperty(value = "物料件号/容器编号", example = "01", required = true)
    @Length(max = 100)
    @NotBlank
    private String no;

    /**
     * 组件实例
     */
    @ApiModelProperty(value = "组件实例id", example = "1")
    private Long componentInstanceId;
}
