package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.mybatis.dataobject.BaseDO;
import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;

/**
 * 设备类型使用日志模板
 *
 * @author yigaohui
 * @date 2024/8/13
 **/
@Data
@ApiModel("设备类型使用日志模板表")
@Validated
public class EquipmentTagUseTemplateAddVO{

    @ApiModelProperty("模板")
    @Size(groups = {InsertValidation.class, UpdateValidation.class}, max = 500, message = "{res_8112034}")
    private String template;

    @ApiModelProperty("操作名称")
    @Size(groups = {InsertValidation.class, UpdateValidation.class}, max = 255, message = "{res_8112035}")
    private String operateName;
}
