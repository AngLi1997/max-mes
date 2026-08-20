package com.bmos.mes.service.process.mapper;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.service.process.dto.ProcedureQueryDTO;
import com.bmos.mes.service.process.dto.ProcedureValidateDTO;
import com.bmos.mes.service.process.dto.query.ProcedurePrincipalQueryDTO;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface ProcedureModelMapper extends BaseMapperX<ProcedureModel> {

    default List<ProcedureModel> selectByProcessIdAndVersion(Long processId, String version) {
        return selectList(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessId, processId)
                .eq(ProcedureModel::getProcessVersion, version));
    }

    void deleteByIdNotIn(@Param("processVersionId") Long processVersionId, @Param("ids") List<Long> ids);


    default List<ProcedureModel> selectListByCondition(ProcedureQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<ProcedureModel>()
                .eqIfPresent(ProcedureModel::getProcessId, dto.getProcessId())
                .eqIfPresent(ProcedureModel::getProcessVersion, dto.getVersion())
                .eqIfPresent(ProcedureModel::getProcessVersionId, dto.getProcessVersionId()));
    }

    default ProcedureModel selectListByCondition2(ProcedurePrincipalQueryDTO dto) {
        return selectOne(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessId, dto.getProcessId())
                .eq(ProcedureModel::getProcessVersion, dto.getProcessVersion())
                .eq(ProcedureModel::getNodeId, dto.getNodeId()));
    }

    default List<String> selectProcessModelList(Long processVersionId) {
        return selectList(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessVersionId, processVersionId)
                .select(Collections.singletonList(ProcedureModel::getProcessModelId)))
                .stream().map(ProcedureModel::getProcessModelId).collect(Collectors.toList());
    }

    void deleteByProcessVersion(@Param("processVersionId") Long processVersionId);

    default boolean existsEmptyProcessModel(Long processVersionId) {
        return exists(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessVersionId, processVersionId)
                .isNull(ProcedureModel::getProcessModelId));
    }

    default List<ProcedureModel> selectByProcessId(Long id) {
        return selectList(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessVersionId, id));
    }

    default List<ProcedureModel> getModeByIdListAndProcessVersionId(Set<Long> procedureModelIdList, Long processVersionId){
        if (CollUtil.isEmpty(procedureModelIdList)){
            return Collections.emptyList();
        }
        return selectList(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessVersionId, processVersionId)
                .in(ProcedureModel::getId,procedureModelIdList));
    }

    default List<ProcedureModel> selectListByNodIdAndProcessVersionId(List<String> procedureModeNodeId, Long processVersionId){
        return selectList(new LambdaQueryWrapperX<ProcedureModel>()
                .in(ProcedureModel::getNodeId,procedureModeNodeId)
                .eq(ProcedureModel::getProcessVersionId,processVersionId));
    }

    default List<ProcedureModel> selectByIds(List<Long> procedureModelIdS){
        return selectList(new LambdaQueryWrapperX<ProcedureModel>()
                .in(ProcedureModel::getId,procedureModelIdS));
    }

    default Integer selectMaxSort(Long id){
        ProcedureModel modelList = selectOne(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessVersionId, id)
                .orderByDesc(ProcedureModel::getSort).last("limit 1"));
        return Optional.ofNullable(modelList.getSort()).orElse(1);
    }

    default ProcedureModel selectByProcessVersionAndProcedureId(Long procedureId, Long processId, String processVersion){
        return selectOne(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessId, processId)
                .eq(ProcedureModel::getProcessVersion, processVersion)
                .eq(ProcedureModel::getProcedureId, procedureId));
    }

    default List<ProcedureModel> selectByProcessVersion(Long processVersionId){
        return selectList(new LambdaQueryWrapperX<ProcedureModel>()
                .eq(ProcedureModel::getProcessVersionId,processVersionId));
    }

    List<NodeVO> getListByDeleteIds(@Param("modelIdS") List<Long> modelId);
}
