package com.bmos.mes.service.components.convert;

import com.bmos.mes.service.components.dto.FormDataOPT;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.components.vo.BusinessComponentInstanceVO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * BusinessComponentInstance mapstruct
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/7/18 15:46
 */
@Mapper
public interface BusinessComponentInstanceConvert {

    BusinessComponentInstanceConvert INSTANCE = Mappers.getMapper(BusinessComponentInstanceConvert.class);

    BusinessComponentInstanceVO convertToVO(BusinessComponentInstance componentInstance);

    default List<FormDataOPT> convertToOPT(List<ExecuteFormData> executeFormData, List<BatchRecordComponent> components, List<ProcedureStepConfig> configs, BusinessComponentInstance componentInstance) {
        Map<Long, ExecuteFormData> formDataValue = executeFormData
                .stream()
                .filter(item -> Objects.equals(item.getCopyVersion(), componentInstance.getCopyVersion()))
                .filter(item -> Objects.equals(item.getBatchNo(), componentInstance.getBatchNo()))
                .filter(item -> Objects.equals(item.getReuse(), componentInstance.getReuse()))
                .filter(item -> {
                    if (!item.getReuse()){
                        return Objects.equals(item.getProcedureStepId(), componentInstance.getProcedureStepId());
                    }else {
                        return true;
                    }
                })
//                .filter(item -> Objects.equals(item.getProcedureStepId(), componentInstance.getProcedureStepId()))
                .collect(Collectors.toMap(ExecuteFormData::getFieldId, Function.identity(), (v1, v2) -> v1));
        return components.stream()
                .map(item -> {
                    FormDataOPT opt = new FormDataOPT(
                            item.getId(),
                            item.getParentId(),
                            item.getFieldId(),
                            item.getComponentType(),
                            item.getComponentDetail(),
                            item.getRecordItemId(),
                            configs.stream()
                                    .filter(conf -> Objects.equals(conf.getProcessId(), componentInstance.getProcessId())
                                                && Objects.equals(conf.getVersion(), componentInstance.getProcessVersion())
                                                && Objects.equals(conf.getProcedureStepModelId(), componentInstance.getReuse() ? 0 : componentInstance.getProcedureStepModelId())
                                                && Objects.equals(conf.getComponentId(), item.getId())
                                    )
                                    .findFirst()
                                    .map(ProcedureStepConfig::getConfigInfo)
                                    .orElse(null));
                    opt.setFormData(formDataValue.get(item.getFieldId()));
                    return opt;
                })
                .collect(Collectors.toList());
    }
}
