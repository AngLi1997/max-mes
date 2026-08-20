package com.bmos.mes.service.process.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum;
import com.bmos.mes.service.process.dto.ProcedureStepDTO;
import com.bmos.mes.service.process.dto.save.ProcedureStepConfigSaveDTO;
import com.bmos.mes.service.process.dto.save.SaveProcessSortVO;
import com.bmos.mes.service.process.model.*;
import com.bmos.mes.service.process.vo.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.bmos.mes.service.process.constant.ProcessConstant.REUSE_PROCEDURE_STEP_ID;

@Mapper
public interface ProcedureStepModelConverter {
    ProcedureStepModelConverter INSTANCE = Mappers.getMapper(ProcedureStepModelConverter.class);


    ProcedureStepModel convert(ProcedureStepDTO dto);

    default List<ProcedureStepVO> convertVOList(List<ProcedureStepModel> procedureStepModels, List<ProcedureStepRole> roles,
                                                List<ProcedureStepSop> sops, Map<Long, ProcedureStep> stepMap) {
        Map<Long, List<Long>> sopList = CollectionUtils.convertMultiMap(sops, ProcedureStepSop::getStepModelId, ProcedureStepSop::getOperationSopId);
        Stream<ProcedureStepVO> stream = procedureStepModels.stream().map(
                e -> {
                    ProcedureStepVO procedureStepVO = convertVO(e);
                    procedureStepVO.setNodeFunction(ProcedureStepNodeFunctionEnum.getEnumByValue(e.getNodeFunction()));
                    procedureStepVO.setStepTaskType(e.getStepType());
                    procedureStepVO.setAreaList(StrUtil.split(e.getArea(), StrUtil.COMMA));
                    procedureStepVO.setEquipmentTypeList(StrUtil.split(e.getEquipment(), StrUtil.COMMA));
                    procedureStepVO.setOperationSopId(Optional.ofNullable(sopList.get(e.getId())).orElse(new ArrayList<>()));
                    procedureStepVO.setHistoricalName(stepMap.get(e.getProcedureStepId()).getName());
                    return procedureStepVO;
                }
        );
        Map<Long, List<Long>> roleMap = CollectionUtils.convertMultiMap(roles, ProcedureStepRole::getProcedureStepId, ProcedureStepRole::getRoleId);
        if (CollUtil.isEmpty(roles)) {
            return stream.collect(Collectors.toList());
        }
        return stream.peek(e -> e.setRoles(roleMap.get(e.getId()))).collect(Collectors.toList());
    }

    default List<ProcedureStepModelVO> convertStepModelVOList(List<ProcedureStepModel> procedureStepModels, List<ProcedureStepRole> roles) {
        Map<Long, List<Long>> roleMap = CollectionUtils.convertMultiMap(roles, ProcedureStepRole::getProcedureStepId, ProcedureStepRole::getRoleId);
        return procedureStepModels.stream().map(
                e -> {
                    ProcedureStepModelVO stepModelVO = convertStepModelVo(e);
                    stepModelVO.setGroupIds(Optional.ofNullable(roleMap.get(e.getId())).orElse(Collections.emptyList()));
                    return stepModelVO;
                }
        ).collect(Collectors.toList());
    }

    ProcedureStepModelVO convertStepModelVo(ProcedureStepModel procedureStepModel);

    @Mapping(target = "nodeFunction", expression = "java(com.bmos.common.base.enums.CommonEnum.getEnumByName(com.bmos.mes.common.enums.process.ProcedureStepNodeFunctionEnum.class, procedureStepModel.getNodeFunction()))")
    ProcedureStepVO convertVO(ProcedureStepModel procedureStepModel);

    default ProcedureStepModel convert(ProcedureModel procedureModel, ProcedureStepDTO procedureStepDTO, Long procedureStepId,
                                       Integer sort) {
        ProcedureStepModel procedureStepModel = convert(procedureStepDTO);
        procedureStepModel.setId(procedureStepId);
        procedureStepModel.setProcessId(procedureModel.getProcessId());
        procedureStepModel.setProcessVersion(procedureModel.getProcessVersion());
        procedureStepModel.setProcedureId(procedureModel.getProcedureId());
        procedureStepModel.setProcedureModelId(procedureModel.getId());
        procedureStepModel.setArea(CollUtil.join(procedureStepDTO.getAreaList(), StrUtil.COMMA));
        procedureStepModel.setEquipment(CollUtil.join(procedureStepDTO.getEquipmentTypeList(), StrUtil.COMMA));
        procedureStepModel.setStepType(procedureStepDTO.getStepType());
        procedureStepModel.setSort(sort);
        return procedureStepModel;
    }

    default List<ProcedureStepConfig> convertConfigLit(ProcedureStepConfigSaveDTO dto) {
        return dto.getComponents()
                .stream()
                .filter(e -> {
                    String configInfo = e.getConfigInfo();
                    return StrUtil.isNotEmpty(configInfo
                            .replace(StrUtil.DELIM_START, StrUtil.EMPTY)
                            .replace(StrUtil.DELIM_END, StrUtil.EMPTY));
                })
                .map(e -> {
                    ProcedureStepConfig config = new ProcedureStepConfig();
                    config.setComponentId(e.getComponentId());
                    config.setConfigInfo(e.getConfigInfo());
                    config.setFieldId(e.getFieldId());
                    ProcedureStepConfig stepConfig = convertConfig(dto, config);
                    if (dto.getReusable()) {
                        stepConfig.setProcedureStepModelId(REUSE_PROCEDURE_STEP_ID);
                    }
                    return stepConfig;
                })
                .collect(Collectors.toList());
    }

    @Mapping(target = "procedureStepId", source = "dto.procedureStepId")
    @Mapping(target = "procedureStepModelId", source = "dto.procedureStepModelId")
    @Mapping(target = "nodeId", source = "dto.nodeId")
    @Mapping(target = "processId", source = "dto.processId")
    @Mapping(target = "version", source = "dto.version")
    @Mapping(target = "recordItemId", source = "dto.recordItemId")
    @Mapping(target = "recordVersionId", source = "dto.recordVersionId")
    ProcedureStepConfig convertConfig(ProcedureStepConfigSaveDTO dto, ProcedureStepConfig config);

    List<ComponentConfigVO> convertComponentVO(List<ProcedureStepConfig> configs);

    default List<ProcedureStepModel> convertList(ProcedureModel procedureModel, List<ProcedureStepDTO> items) {
        return items.stream().map(e -> {
            ProcedureStepModel step = convert(e);
            step.setArea(CollUtil.join(e.getAreaList(), StrUtil.COMMA));
            step.setEquipment(CollUtil.join(e.getEquipmentTypeList(), StrUtil.COMMA));
            return step;
        }).peek(e -> {
            e.setProcedureId(procedureModel.getProcedureId());
            e.setProcedureModelId(procedureModel.getId());
            e.setProcessVersion(procedureModel.getProcessVersion());
            e.setProcessId(procedureModel.getProcessId());
        }).collect(Collectors.toList());
    }

    default ProcedureStepRecordItemVO convert(ProcedureStepModel stepModel, List<ComponentConfigDetailVO> configs) {
        ProcedureStepRecordItemVO vo = new ProcedureStepRecordItemVO();
        vo.setRecordVersionId(stepModel.getRecordVersionId());
        vo.setRecordItemId(stepModel.getRecordItemId());
        vo.setReusable(stepModel.getReusable());
        vo.setComponentConfigs(configs);

        vo.setProcedureStepId(stepModel.getProcedureStepId());
        vo.setProcedureStepModelId(stepModel.getId());
        vo.setNodeId(stepModel.getNodeId());
        vo.setProcedureModelId(stepModel.getProcedureModelId());
        return vo;
    }

    List<ProcessSortVO> convertToStepModelSortList(List<ProcedureStepModel> list);

    List<ProcedureStepModel> convertToStepModel(List<SaveProcessSortVO> processSortVOS);

    List<ProcedureStepModelListVO> convert2ListVO(List<ProcedureStepModel> nodeListByProcedureModeId);
}
