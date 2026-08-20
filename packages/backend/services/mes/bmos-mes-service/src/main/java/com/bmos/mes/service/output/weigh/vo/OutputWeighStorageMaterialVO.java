package com.bmos.mes.service.output.weigh.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/18 13:36
 */
@Data
@ApiModel("产出称重物料件")
public class OutputWeighStorageMaterialVO {

    /**
     * 物料件id
     */
    @ApiModelProperty(value = "物料件id", example = "1")
    private Long id;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "01")
    private String storageMaterialNo;

    /**
     * 批次编号
     */
    @ApiModelProperty(value = "批次编号", example = "B1")
    private String storageMaterialBatchNo;

    /**
     * 批次id
     */
    @ApiModelProperty(value = "批次id", example = "1")
    private Long storageMaterialBatchId;

    /**
     * 单位id
     */
    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    /**
     * 单位名称
     */
    @ApiModelProperty(value = "单位名称", example = "kg")
    private String unit;

    /**
     * 有效期
     */
    @ApiModelProperty(value = "有效期", example = "2024-04-01")
    private LocalDate expiredDate;

    /**
     * 物料编码
     */
    @ApiModelProperty(value = "物料编码", example = "NaCl")
    private String materialCode;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    /**
     * 皮重
     */
    @ApiModelProperty(value = "皮重", example = "1.00")
    @DecimalMin("0.000000001")
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
     * 物料量
     */
    @ApiModelProperty(value = "物料量", example = "1.00")
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    private BigDecimal quantity;

    /**
     * 签名状态
     */
    @ApiModelEnumProperty(value = "是否已签名", enumClass = WeighSignStatus.class)
    @EnumValidate(WeighSignStatus.class)
    private WeighSignStatus signStatus;

    /**
     * 容器名称
     */
    @ApiModelProperty(value = "容器名称", example = "不锈钢盆")
    private String containerName;

    /**
     * 容器id
     */
    @ApiModelProperty(value = "容器id", example = "1")
    private Long containerId;

    /**
     * 称量人id
     */
    @ApiModelProperty(value = "称量人id", example = "1", required = true)
    private String weigherId;

    /**
     * 称量人姓名-称量人帐号
     */
    @ApiModelProperty(value = "称量人姓名-称量人帐号", example = "张三-123456789")
    private String weigherFullName;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    private String reCheckerId;

    /**
     * 复核人姓名-复核人帐号
     */
    @ApiModelProperty(value = "复核人姓名-复核人帐号", example = "张三-123456789")
    private String reCheckerFullName;

    /**
     * 称量时间
     */
    @ApiModelProperty(value = "称量时间", example = "2024-04-01 13:30:00")
    private LocalDateTime weighTime;

    /**
     * 货位名称
     */
    @ApiModelProperty(value = "货位名称", example = "1")
    private String materialPositionName;
}
