package com.bmos.mes.service.weigh.centre.input.vo;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.common.enums.ingredient.WeighInputStatus;
import com.bmos.unit.service.UnitCache;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物料投入记录vo
 * @author liang
 * @version 1.0.0
 * @date 2024/7/18 15:30
 */
@Data
@ApiModel("物料投入记录vo")
public class WeighInputRecordVO {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "物料名称", example = "氯化钠")
    private String materialName;

    @ApiModelProperty(value = "物料编码", example = "CNA")
    private String materialMergeCode;

    @ApiModelProperty(value = "物料批次号", example = "CNA-20230718-0001")
    private String storageMaterialBatchNo;

    @ApiModelProperty(value = "物料件号", example = "100")
    private String storageMaterialNo;

    @ApiModelProperty(value = "预定量", example = "1")
    @JsonIgnore
    private BigDecimal reserveQuantity;

    @ApiModelProperty(value = "可用量", example = "1")
    @JsonIgnore
    private BigDecimal availableQuantity;

    @ApiModelProperty(value = "数量", example = "100")
    private BigDecimal quantity;

    @ApiModelProperty(value = "单位id", example = "1")
    private Long unitId;

    @ApiModelProperty(value = "单位", example = "克")
    private String unit;

    @ApiModelEnumProperty(value = "投料状态", enumClass = WeighInputStatus.class)
    private WeighInputStatus weighInputStatus;

    @ApiModelProperty(value = "投料人", example = "张三")
    private String inputUserName;

    @ApiModelProperty(value = "投料时间", example = "2023-07-18 15:30:00")
    private LocalDateTime inputTime;

    @ApiModelProperty(value = "称量设备名称", example = "称量设备1")
    private String deviceName;

    @ApiModelProperty(value = "称量设备编码", example = "CNA-20230718-0001")
    private String deviceCode;

    /**
     * 投料组件id
     */
    @ApiModelProperty(value = "投料组件id", example = "1", hidden = true)
    @JsonIgnore
    private Long inputComponentInstanceId;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}
