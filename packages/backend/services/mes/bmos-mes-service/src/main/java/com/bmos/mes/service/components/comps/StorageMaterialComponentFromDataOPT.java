package com.bmos.mes.service.components.comps;

import com.bmos.mes.service.components.annotations.BmosComponentDetail;
import lombok.Data;

import static com.bmos.mes.common.enums.record.BusinessComponentTypeEnum.*;

/**
 * 物料件组件
 * @author liang
 * @version 1.0.0
 * @date 2024/9/10 16:58
 */
@Data
public class StorageMaterialComponentFromDataOPT {

    @BmosComponentDetail(MATERIAL_INFO_MATERIAL_NAME)
    private String materialName;

    @BmosComponentDetail(MATERIAL_INFO_MATERIAL_CODE)
    private String materialCode;

    @BmosComponentDetail(MATERIAL_INFO_MATERIAL_BATCHNO)
    private String materialBatchNo;

    @BmosComponentDetail(MATERIAL_INFO_MATERIAL_PARTNO)
    private String no;

    @BmosComponentDetail(MATERIAL_INFO_MATERIAL_QUANTITY)
    private String quantity;

    @BmosComponentDetail(MATERIAL_INFO_NET_WEIGHT)
    private String netWeight;

    @BmosComponentDetail(MATERIAL_INFO_TARE_WEIGHT)
    private String tareWeight;

    @BmosComponentDetail(MATERIAL_INFO_GROSS_WEIGHT)
    private String grossWeight;

    @BmosComponentDetail(MATERIAL_INFO_UNIT)
    private String unit;
}
