package com.bmos.mes.service.ingredient.weigh.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ApiModel("称量日志分页VO")
@Data
public class WeighLogPageVO {

    /**
     * id
     */
    @ApiModelProperty("id")
    private Long id;

    /**
     * 物料件号
     */
    @ApiModelProperty("物料件号")
    private String materialNo;

    /**
     * 净重
     */
    @ApiModelProperty("净重")
    private BigDecimal netWeight;

    /**
     * 皮重
     */
    @ApiModelProperty("皮重")
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    @ApiModelProperty("毛重")
    private BigDecimal grossWeight;

    /**
     * 单位名称
     */
    @ApiModelProperty("单位名称")
    private String unitName;

    /**
     * 称量类型
     */
    @ApiModelEnumProperty(value = "称量类型", enumClass = WeighType.class)
    @EnumValidate(WeighType.class)
    private WeighType weighType;

    /**
     * 称量人 名称
     */
    @ApiModelProperty("称量人名称")
    private String weigherName;

    /**
     * 称量人 loginName
     */
    @ApiModelProperty("称量人loginName")
    private String weigherLoginName;

    /**
     * 复核人名称
     */
    @ApiModelProperty("复核人名称")
    private String reCheckerName;

    /**
     * 复核人loginName
     */
    @ApiModelProperty("复核人loginName")
    private String reCheckerLoginName;

    /**
     * 称量时间
     */
    @ApiModelProperty("称量时间")
    private LocalDateTime weighTime;

    /**
     * 物料名称
     */
    @ApiModelProperty("物料名称")
    private String materialName;

    /**
     * 物料编码
     */
    @ApiModelProperty("物料编码")
    private String materialMergeCode;

    /**
     * 物料批号
     */
    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    /**
     * 设备名称
     */
    @ApiModelProperty("设备名称")
    private String equipmentName;

    /**
     * 设备编号
     */
    @ApiModelProperty("设备编号")
    private String equipmentCode;

    /**
     * 校准状态
     */
    @ApiModelProperty("校准状态")
    private Boolean equipmentStatus;

    /**
     * 校准效期
     */
    @ApiModelProperty("校准效期")
    private LocalDate equipmentExpireDate;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 产品编码
     */
    @ApiModelProperty("产品编码")
    private String productMergeCode;

    /**
     * 生产批号
     */
    @ApiModelProperty("生产批号")
    private String productBatchNo;

    /**
     * 物料类型
     */
    @ApiModelProperty("物料类型")
    private Integer materialType;

}
