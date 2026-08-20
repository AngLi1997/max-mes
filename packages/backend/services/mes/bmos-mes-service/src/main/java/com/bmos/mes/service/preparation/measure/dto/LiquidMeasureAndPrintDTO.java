package com.bmos.mes.service.preparation.measure.dto;

import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.enums.preparation.MeasureModeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@ApiModel("量取打码DTO")
public class LiquidMeasureAndPrintDTO {

    @ApiModelProperty(value = "量取量", required = true)
    @NotNull
    private BigDecimal measureQuantity;

    @ApiModelProperty(value = "量取单位", required = true)
    @NotNull
    private Long unitId;

    @ApiModelProperty(value = "量取批次id", required = true)
    @NotNull
    private Long measureBatchId;

    @ApiModelProperty("容器id")
    private Long containerId;

    @ApiModelProperty("货位id")
    private Long materialPositionId;

    @ApiModelProperty("量取模式")
    @ApiModelEnumProperty(value = "量取模式", required = true, enumClass = MeasureModeEnum.class)
    @NotEmpty
    private String measureMode;

    public void validateQuantity() {
        if (measureQuantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new BmosException(MesResponseCode.MEASURE_QUANTITY_MUST_GRATER_THAN_ZERO);
        }
    }

}
