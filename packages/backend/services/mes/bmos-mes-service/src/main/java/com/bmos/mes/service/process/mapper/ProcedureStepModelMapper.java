package com.bmos.mes.service.process.mapper;

import cn.hutool.core.util.ObjectUtil;
import com.bmos.mes.service.execute.dto.IntactMergeListQueryDTO;
import com.bmos.mes.service.execute.vo.IntactFormDataVO;
import com.bmos.mes.service.process.dto.*;
import com.bmos.mes.service.process.dto.query.CalculateDataQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessRecordOrderQueryDTO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.vo.*;
import com.bmos.mes.service.process.vo.Task.NodeVO;
import com.bmos.mes.service.workflow.vo.ProcedureStepDurationVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProcedureStepModelMapper extends BaseMapperX<ProcedureStepModel> {

    default List<ProcedureStepModel> selectByProcedureModelId(Long procedureModelId) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcedureModelId, procedureModelId));
    }

    void deleteByIdNotIn(@Param("procedureModelId") Long procedureModelId, @Param("ids") List<Long> ids);

    default List<ProcedureStepModel> selectByProcedureModelIds(List<Long> procedureModelIds) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepModel>().in(ProcedureStepModel::getProcedureModelId, procedureModelIds));
    }

    List<ProcessRecordVO> selectRecords(ProcessRecordOrderQueryDTO dto);

    default ProcedureStepModel selectOneByCondition(ProcedureStepRecordItemQueryDTO dto) {
        return selectOne(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcessId, dto.getProcessId())
                .eq(ProcedureStepModel::getProcessVersion, dto.getProcessVersion())
                .eq(ProcedureStepModel::getNodeId, dto.getNodeId()));
    }

    List<ProcessStepVO> selectListByProcess(ProcessStepQueryDTO dto);

    List<FieldConfigVO> selectFieldsConfig(@Param("query") CalculateDataQueryDTO query, @Param("fieldIds") List<Long> fieldIds);

    List<IntactFormDataVO> selectRecordContents(IntactMergeListQueryDTO dto);

    void deleteByProcedureModelId(@Param("procedureModelId") Long procedureModelId);

    List<ProcessRecordItemVO> queryRecordVersionIdByProcessId(@Param("processId") Long processId, @Param("processVersion") String processVersion,
                                                              @Param("modelList") List<Long> modelId);

    List<ProcedureStepDurationVO> selectDurationByNodeIds(@Param("procedureStepNodeIdList") List<String> procedureStepNodeIdList);

    default List<ProcedureStepModel> getStepModelByProcessIdAndVersion(Long processId, String modifyBeforeVersion) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcessId, processId)
                .eq(ProcedureStepModel::getProcessVersion, modifyBeforeVersion));
    }

    default List<ProcedureStepModel> getNodeListByProcedureModeId(Long id) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcedureModelId, id));
    }

    default List<ProcedureStepModel> getStepModelByProcessIdAndVersionAndNodeIdList(Long processId, String processVersion) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcessId, processId)
                .eq(ProcedureStepModel::getProcessVersion, processVersion));
    }

    default ProcedureStepModel selectListByProcedureStepIdAndProcessIdAndVersion(Long procedureStepId,
                                                                                 Long processId, String processVersion) {
        return selectOne(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcedureStepId, procedureStepId)
                .eq(ProcedureStepModel::getProcessId, processId)
                .eq(ProcedureStepModel::getProcessVersion, processVersion));
    }

    List<ProcedureStepDurationVO> selectDurationByStepModeIds(@Param("procedureStepModelIdList") List<Long> procedureStepModelIdList);

    default List<ProcedureStepModel> queryModelListByProcedureModeIdAndProcessId(Long procedureModelId, Long processId, String processVersion) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcedureModelId, procedureModelId)
                .eq(ProcedureStepModel::getProcessId, processId)
                .eq(ProcedureStepModel::getProcessVersion, processVersion));
    }

    default List<ProcedureStepModel> queryListByRecordItemIdAndProcessIdAndVersion(Long recordItemId, Long processId, String processVersion, Boolean reusable) {
        return selectList(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getRecordItemId, recordItemId)
                .eq(ProcedureStepModel::getProcessId, processId)
                .eq(ProcedureStepModel::getProcessVersion, processVersion)
                .eq(ProcedureStepModel::getReusable, reusable));
    }

    /**
     * 查询工步最大排序号默认返回1
     *
     * @param id             工序模型id
     * @param processVersion 工艺版本
     * @param processId      工艺id
     * @return
     */
    default Integer selectByMaxSort(Long id, String processVersion, Long processId) {
        ProcedureStepModel model = selectOne(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcedureModelId, id)
                .eq(ProcedureStepModel::getProcessId, processId)
                .eq(ProcedureStepModel::getProcessVersion, processVersion)
                .orderByDesc(ProcedureStepModel::getSort)
                .last("limit 1"));
        return ObjectUtil.isEmpty(model) || ObjectUtil.isNull(model.getSort()) ? 1 : model.getSort() + 1;
    }

    List<ProcedureStepModelDetailVO> queryStepModelList(ProcedureStepModelQueryDTO dto);

    List<IntactFormDataVO> selectRecordContentsByNodeFunction(IntactMergeListQueryDTO queryDTO);

    default List<ProcedureStepModel> selectByProcessAndRecordItemId(Long processId, Long recordItemId){
        return selectList(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcessId, processId)
                .eq(ProcedureStepModel::getRecordItemId, recordItemId));
    }

    default ProcedureStepModel selectByStepId(Long processId, String processVersion, Long procedureStepId){
        return selectOne(new LambdaQueryWrapperX<ProcedureStepModel>()
                .eq(ProcedureStepModel::getProcessId, processId)
                .eq(ProcedureStepModel::getProcessVersion, processVersion)
                .eq(ProcedureStepModel::getProcedureStepId, procedureStepId));
    }

    List<NodeVO> queryByIds(@Param("ids") List<Long> deleteTaskId);

    /**
     * 根据工艺id和工艺版本id和记录id查询工步信息
     * @param queryDTOS
     * @return
     */
    List<ProcedureStepModel> selectByProcessAndRecord(@Param("queryDTOList") List<ProcessRecordQueryDTO> queryDTOS);
}
