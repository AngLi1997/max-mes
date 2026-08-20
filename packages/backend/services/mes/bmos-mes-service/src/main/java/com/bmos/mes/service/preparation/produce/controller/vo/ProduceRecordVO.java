package com.bmos.mes.service.preparation.produce.controller.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.preparation.PrepareInputStatusEnum;
import com.bmos.mes.common.enums.preparation.PrepareSignStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 配液产出结果VO
 */
@Getter
@Setter
@ApiModel("配液产出物料件VO")
public class ProduceRecordVO {
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
    @ApiModelProperty(value = "物料量", example = "1.00")
    @DecimalMin("0.000000001")
    @DecimalMax("9999999999.999999999")
    private BigDecimal quantity;

    /**
     * 签名状态
     */
    @ApiModelEnumProperty(value = "是否已签名", enumClass = PrepareInputStatusEnum.class)
    @EnumValidate(PrepareInputStatusEnum.class)
    private PrepareSignStatusEnum signStatus;

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
     * 产出人id
     */
    @ApiModelProperty(value = "产出人id", example = "1", required = true)
    private String producerId;

    /**
     * 产出人姓名
     */
    @ApiModelProperty(value = "产出人姓名", example = "张三")
    private String producerName;

    /**
     * 产出人姓名-产出人帐号
     */
    @ApiModelProperty(value = "产出人姓名-产出人帐号", example = "张三-123456789")
    private String producerFullName;

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
     * 复核人姓名
     */
    @ApiModelProperty(value = "复核人姓名", example = "张三")
    private String reCheckerName;

    /**
     * 产出时间
     */
    @ApiModelProperty(value = "产出时间", example = "2024-04-01 13:30:00")
    private LocalDateTime produceTime;

    /**
     * 货位名称
     */
    @ApiModelProperty(value = "货位名称", example = "1")
    private String materialPositionName;

    /**
     * 产出排序
     */
    @ApiModelProperty(value = "产出排序", example = "1")
    private Integer sort;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格", example = "1")
    private String specification;

}
