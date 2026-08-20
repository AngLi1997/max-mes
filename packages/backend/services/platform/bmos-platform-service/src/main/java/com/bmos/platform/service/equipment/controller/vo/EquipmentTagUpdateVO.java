package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 设备tag新增Vo
 *
 * @author yigaohui
 * @date 2024/8/12
 **/
@Data
@Validated
@ApiModel("设备类型编辑VO")
public class EquipmentTagUpdateVO {

    @ApiModelProperty("id")
    @NotNull(groups = {UpdateValidation.class},message = "{res_8112035}")
    private Long id;
    /**
     * 设备信息配置列表
     */
    @ApiModelProperty("设备信息配置列表")
    private List<EquipmentPropertyAddVO> infoPropertyList;
    /**
     * 设备数据配置列表
     */
    @ApiModelProperty("设备数据配置列表")
    private List<EquipmentPropertyAddVO> dataPropertyList;

    @ApiModelProperty("设备状态列表")
    private List<EquipmentPropertyAddVO> statusPropertyList;


    @ApiModelProperty("描述")
    @Size(groups = {InsertValidation.class, UpdateValidation.class}, max = 500, message = "{res_8112034}")
    private String description;

    @ApiModelProperty("使用日志模板")
    private List<EquipmentTagUseTemplateAddVO> useTemplateList;
}
