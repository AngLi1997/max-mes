package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
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
@ApiModel("设备类型新增VO")
public class EquipmentTagAddVO {
    /**
     * 标签编码
     */
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class}, message = "{res_8112043}")
    @Size(groups = {InsertValidation.class, UpdateValidation.class}, max = 128, message = "{res_8112042}")
    @ApiModelProperty("类型编码")
    private String code;
    /**
     * tag名称
     */
    @NotEmpty(groups = {InsertValidation.class, UpdateValidation.class}, message = "{res_8112032}")
    @Size(groups = {InsertValidation.class, UpdateValidation.class}, max = 128, message = "{res_8112033}")
    @ApiModelProperty("类型名称")
    private String name;

    @ApiModelProperty("父级id")
    private Long parentId;

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
