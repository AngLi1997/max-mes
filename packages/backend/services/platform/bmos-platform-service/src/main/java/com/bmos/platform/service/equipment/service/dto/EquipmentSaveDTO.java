package com.bmos.platform.service.equipment.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * 设备新建DTO
 */
@Getter
@Setter
@ApiModel("设备新建参数")
public class EquipmentSaveDTO {

    @ApiModelProperty(value = "设备分类ID",required = true)
    @NotNull
    private Long categoryId;

    /**
     * 设备code
     */
    @ApiModelProperty(value = "设备code", required = true)
    @NotBlank
    private String code;

    /**
     * 设备名称
     */
    @ApiModelProperty(value = "设备名称", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty(value = "设备描述")
    private String description;

    /**
     * 设备标签id集合
     */
    @ApiModelProperty(value = "设备标签id集合", required = true)
    @NotNull
    private List<Long> tagIdList;

    /**
     * 设备所属标签的设备状态配置
     */
    @ApiModelProperty(value = "设备所属标签的设备状态配置")
    private List<EquipmentStatusDTO> tagEquipmentStatusDTOList;

    /**
     * 设备下标签的属性配置
     */
    @ApiModelProperty(value = "设备下标签的属性配置")
    private List<EquipmentPropertyDTO> equipmentPropertyDTOList;


    @ApiModelProperty("设备数据属性配置")
    private List<EquipmentPropertyDTO> equipmentDataPropertyDTOList;

}
