package com.bmos.mes.service.process.service.condition;

import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.record.business.model.ProductFormulaInfo;
import com.bmos.mes.service.storage.manage.model.StorageMaterialReserve;
import com.bmos.mes.service.storage.manage.vo.BatchReservedMaterialVO;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.factory.vo.RoomInfoFeignVO;
import com.bmos.unit.service.UnitCache;
import lombok.Data;

import java.util.List;

/**
 * 条件判定上下文
 *
 * @author yigaohui
 * @date 2024/7/9
 **/
@Data
public class ConditionCalculateContext {
    private List<EquipmentInfoFeignVO> equipmentStatusFeignVOList;

    private List<RoomInfoFeignVO> roomInfoFeignVOList;

    private Plan plan;

    private ProductFormulaInfo productFormulaInfo;

    private List<BatchReservedMaterialVO> reserveList;
    private UnitCache unitCache;
}
