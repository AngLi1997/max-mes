package com.bmos.mes.service.record.business.strategy;

import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 设备信息-设备名称组件
 *
 * @author yigaohui
 * @date 2024/4/23
 **/
@Service("EQUIPMENT_INFO_NAME")
public class EquipmentNameComponentStrategy implements BusinessComponentStrategy {
    @Override
    public void handleBusinessComponent(@NotNull List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
        convert.setFieldId(component.getFieldId());
        convert.setComponentType(component.getComponentType());
        convert.setValue(info.getEquipmentInfo().getName());
        results.add(convert);
    }
}
