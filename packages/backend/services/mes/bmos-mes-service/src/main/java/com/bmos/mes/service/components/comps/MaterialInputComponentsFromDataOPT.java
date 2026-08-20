package com.bmos.mes.service.components.comps;

import cn.hutool.extra.spring.SpringUtil;
import com.bmos.mes.service.components.annotations.BmosComponentDetail;
import com.bmos.mes.service.components.annotations.BmosComponentSummary;
import com.bmos.mes.service.components.annotations.BmosComponentSummaryConfig;
import com.bmos.mes.service.components.annotations.BmosComponentSummaryGroupBy;
import com.bmos.mes.service.components.enums.BmosComponentSummaryConfigFilter;
import com.bmos.mes.service.components.enums.BmosComponentSummaryType;
import com.bmos.unit.service.UnitCache;
import lombok.Data;

import java.math.BigDecimal;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.*;

/**
 * 物料投入组件
 * @author liang
 * @version 1.0.0
 * @date 2024/7/30 17:32
 */
@Data
@BmosComponentSummaryConfig(
        value = MATERIAL_INPUT_SUMMARY,
        filter = BmosComponentSummaryConfigFilter.MATERIAL_INPUT
)
public class MaterialInputComponentsFromDataOPT {

    /**
     * 物料投入id
     */
    @BmosComponentSummary(value = MATERIAL_INPUT_SUMMARY_TOTAL_NUMBER, summaryType = BmosComponentSummaryType.SIZE)
    private Long id;

    /**
     * 配方物料id
     */
    @BmosComponentSummaryGroupBy
    private Long formulaMaterialId;

    /**
     * 物料名称
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_MATERIAL_NAME)
    @BmosComponentSummary(value = MATERIAL_INPUT_SUMMARY_MATERIAL_NAME, summaryType = BmosComponentSummaryType.STATIC)
    private String materialName;

    /**
     * 合并编码
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_MATERIAL_CODE)
    @BmosComponentSummary(value = MATERIAL_INPUT_SUMMARY_MATERIAL_CODE, summaryType = BmosComponentSummaryType.STATIC)
    private String mergeCode;

    /**
     * 物料规格
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_MATERIAL_SPECIFICATION)
    @BmosComponentSummary(value = MATERIAL_INPUT_SUMMARY_MATERIAL_SPECIFICATION, summaryType = BmosComponentSummaryType.STATIC)
    private String specification;

    /**
     * 物料批号
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_MATERIAL_BATCHNO)
    private String storageMaterialBatchNo;

    /**
     * 物料件号
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_MATERIAL_PARTNO)
    private String storageMaterialNo;

    /**
     * 数量
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_MATERIAL_QUANTITY)
    @BmosComponentSummary(value = MATERIAL_INPUT_SUMMARY_TOTAL_QUANTITY, summaryType = BmosComponentSummaryType.SUM)
    private BigDecimal quantity;

    /**
     * 单位id
     */
    private Long unitId;

    /**
     * 单位
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_UNIT)
    @BmosComponentSummary(value = MATERIAL_INPUT_SUMMARY_UNIT, summaryType = BmosComponentSummaryType.STATIC)
    private String unit;


    /**
     * 投料人
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_FEEDER)
    private String inputUserName;

    /**
     * 投料时间
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_FEEDING_TIME)
    private String inputTime;

    /**
     * 设备名称
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_DEVICE_NAME)
    private String deviceName;

    /**
     * 设备编码
     */
    @BmosComponentDetail(MATERIAL_INPUT_DETAILS_DEVICE_CODE)
    private String deviceCode;

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
        this.unit = SpringUtil.getBean(UnitCache.class).getGlobalUnitName(unitId);
    }
}
