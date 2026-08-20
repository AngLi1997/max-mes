package com.bmos.mes.service.output.weigh.dto;

import com.bmos.common.exception.BmosException;
import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.WeighMode;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 称量打码dto(产出称量)
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 15:54
 */
@ApiModel("产出称量打码dto")
@Data
public class OutputWeighAndPrintDTO {

    /**
     * 产出称量流程id
     */
    @ApiModelProperty(value = "产出称量流程id", example = "1", required = true)
    @NotNull
    private Long outputWeighProcessId;

    /**
     * 是否为按件称量
     */
    @ApiModelProperty(value = "是否为按件称量", example = "false")
    private Boolean byPiece = false;

    /**
     * 件重
     */
    @ApiModelProperty(value = "物料量", example = "1.00")
    @DecimalMin("0")
    @DecimalMax("9999999999.999999999")
    private BigDecimal quantity;

    /**
     * 皮重
     */
    @ApiModelProperty(value = "皮重", example = "1.00")
    @DecimalMin("0")
    @DecimalMax("9999999999.999999999")
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    @ApiModelProperty(value = "毛重", example = "1.00")
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    private BigDecimal grossWeight;

    /**
     * 净重
     */
    @ApiModelProperty(value = "净重", example = "1.00")
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    private BigDecimal netWeight;

    /**
     * 称量单位id
     */
    @ApiModelProperty(value = "称量单位id", example = "1")
    @NotNull
    private Long unitId;

    /**
     * 容器id
     */
    @ApiModelProperty(value = "容器id", example = "1")
    private Long containerId;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1")
    private Long materialPositionId;

    /**
     * 称量模式
     */
    @ApiModelEnumProperty(value = "称量模式", enumClass = WeighMode.class, required = true)
    @EnumValidate(WeighMode.class)
    @NotNull
    private Integer weighMode;

    @ApiModelProperty("称量设备id")
    private Long deviceId;

    @ApiModelProperty(value = "产出件数(默认1件)", example = "1")
    private Integer size = 1;

    public void validateNetWeight() {
        if (!byPiece && netWeight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BmosException(MesResponseCode.NET_WEIGH_MUST_GREATER_THAN_ZERO);
        }
    }
}
