package com.bmos.mes.service.record.business.model.preparation;

import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 配页投入所需要的回填批记录的信息
 */
@Getter
@Setter
public class PreparationInputDetailInfo extends ProductionDetailInfo {

    /**
     * 当前投料时的物料信息
     */
    private List<PreparationInputMaterialInfo> currentInputStoratageMaterialList;

    /**
     * 之前所投的物料信息
     */
    private List<PreparationInputMaterialInfo> preInputStorageMaterialList;
}
