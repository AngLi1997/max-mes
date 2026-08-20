package com.bmos.platform.service.equipment.controller.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.platform.service.equipment.service.enums.AcquisitionPointDataTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.web.validation.InsertValidation;
import com.bmos.web.validation.UpdateValidation;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 标签属性VO
 */
@Getter
@Setter
@ApiModel("标签属性VO")
public class EquipmentPropertyAcquisitionPointVO extends EquipmentPropertyVO{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编码", required = true)
    @NotNull
    private String acquisitionPointCode;

    @ApiModelProperty(value = "名称", required = true)
    private String acquisitionPointName;

    @ApiModelProperty(value = "数据点位名称", required = true)
    private String dataPointName;

    @ApiModelEnumProperty(value = "数据类型", enumClass = AcquisitionPointDataTypeEnum.class, required = true)
    private AcquisitionPointDataTypeEnum AcquisitionPointDataType;
}
