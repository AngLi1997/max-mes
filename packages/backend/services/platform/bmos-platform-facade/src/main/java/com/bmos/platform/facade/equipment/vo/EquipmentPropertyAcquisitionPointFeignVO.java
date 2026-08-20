package com.bmos.platform.facade.equipment.vo;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.platform.facade.equipment.enums.AcquisitionPointDataTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 标签属性VO
 */
@Getter
@Setter
@ApiModel("标签属性VO")
public class EquipmentPropertyAcquisitionPointFeignVO extends EquipmentPropertyFeignVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编码", required = true)
    @NotNull
    private String acquisitionPointCode;

    @ApiModelProperty(value = "名称", required = true)
    private String acquisitionPointName;

    @ApiModelProperty(value = "数据点位名称", required = true)
    private String dataPointName;

    /**
     * {@link AcquisitionPointDataTypeEnum}
     */
    private String acquisitionPointDataType;

    public AcquisitionPointDataTypeEnum getAcquisitionPointDataTypeEnum() {
        return CommonEnum.getEnumByValue(AcquisitionPointDataTypeEnum.class, acquisitionPointDataType);
    }

    public void setAcquisitionPointDataType(AcquisitionPointDataTypeEnum acquisitionPointDataType){
        if (acquisitionPointDataType != null){
            this.acquisitionPointDataType = acquisitionPointDataType.getValue();
        }
    }
}
