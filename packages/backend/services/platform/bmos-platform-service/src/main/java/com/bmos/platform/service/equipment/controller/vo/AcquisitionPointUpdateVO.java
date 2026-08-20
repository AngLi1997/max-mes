package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.platform.service.equipment.enums.AcquisitionPlatformEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author yigaohui
 * @date 2024/4/20
 **/
@Data
@ApiModel("采集点位修改DTO")
public class AcquisitionPointUpdateVO{

    @ApiModelProperty(value = "主键id", required = true)
    @NotNull(message = "{acquisitionPoint.id.null}", groups = UpdateValidation.class)
    private Long id;

    @ApiModelProperty(value = "名称", required = true)
    @NotEmpty(message = "{res_8112014}", groups = {InsertValidation.class, UpdateValidation.class})
    @Size(max = 255, message = "{res_8112013}")
    private String name;

    @ApiModelProperty(value = "数据点位名称", required = true)
    @NotEmpty(message = "{res_8112016}", groups = {InsertValidation.class, UpdateValidation.class})
    @Size(max = 255, message = "{res_8112017}")
    private String dataPointName;

    @ApiModelEnumProperty(value = "数据类型", enumClass = AcquisitionPointDataTypeEnum.class, required = true)
    @EnumValidate(value = AcquisitionPointDataTypeEnum.class)
    @NotNull(message = "{res_8112020}", groups = {InsertValidation.class, UpdateValidation.class})
    private AcquisitionPointDataTypeEnum dataType;

    /**
     * 采集点类型
     */
    @ApiModelEnumProperty(value = "采集点类型", enumClass = AcquisitionPointTypeEnum.class, required = true)
    @EnumValidate(value = AcquisitionPointDataTypeEnum.class)
    @NotNull(message = "{res_8112019}", groups = {InsertValidation.class, UpdateValidation.class})
    private AcquisitionPointTypeEnum type;

    @ApiModelProperty(value = "描述")
    @Size(max = 500, message = "{res_8112018}", groups = {InsertValidation.class, UpdateValidation.class})
    private String description;

    @ApiModelEnumProperty(value = "数采平台",enumClass = AcquisitionPlatformEnum.class, required = true)
    @NotNull(message = "{res_8114039}",groups = {InsertValidation.class, UpdateValidation.class})
    private AcquisitionPlatformEnum acquisitionPlatform;
}
