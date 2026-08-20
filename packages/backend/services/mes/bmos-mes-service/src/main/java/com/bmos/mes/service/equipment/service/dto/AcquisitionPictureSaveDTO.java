package com.bmos.mes.service.equipment.service.dto;

import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@ApiModel("设备数采绘图保存DTO")
@Data
public class AcquisitionPictureSaveDTO {

    @ApiModelProperty("绘图截图转成的base64")
    @NotBlank
    private String picture;

    @ApiModelProperty("文件后缀")
    @NotEmpty
    private String suffix;

    @ApiModelProperty("子组件fieldId,即点击执行的组件fieldId")
    @NotNull
    private Long fieldId;

    @ApiModelProperty("生产计划id")
    @NotNull
    private Long productPlanId;

    @ApiModelProperty("工步模型id")
    @NotNull
    private Long procedureStepModelId;

    @ApiModelProperty("copyVersion")
    @NotNull
    private Long copyVersion;

    @NotEmpty
    @ApiModelProperty(value = "设备信息", example = "FI002-xxx罐子名称")
    private String equipmentInfo;

    @NotEmpty
    @ApiModelProperty(value = "设备数据", example = "搅拌速度-speed")
    private String equipmentData;

    @NotNull
    @ApiModelProperty("设备id")
    private Long equipmentId;
}
