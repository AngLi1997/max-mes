package com.bmos.lims2.server.eln.record.component;

import com.bmos.lims2.server.eln.entry.converter.ExecuteFormDataConverter;
import com.bmos.lims2.server.eln.entry.dto.ElnEntryContext;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.record.vo.ComponentListVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户手写签名
 */
@Service("HANDLE_SUBMIT_SIGN")
public class UserSubmitSignComponentStrategy implements BusinessComponentStrategy {
    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component, ElnEntryContext info) {
        ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(info.getDto());
        convert.setValue(info.getSignInfo().getSignUrl());
        convert.setOperationUser(info.getSignInfo().getUserId());
        convert.setFieldId(component.getFieldId());
        convert.setComponentType(component.getComponentType());
        results.add(convert);
    }
}
