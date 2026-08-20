package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 用户手写签名
 */
@Service("HANDLE_SUBMIT_SIGN")
public class UserSubmitSignComponentStrategy  implements BusinessComponentStrategy {
    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component, ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(info.getDto());
        convert.setValue(info.getSignInfo().getSignUrl());
        convert.setOperationUser(info.getSignInfo().getUserId());
        convert.setFieldId(component.getFieldId());
        convert.setComponentType(component.getComponentType());
        results.add(convert);
    }
}
