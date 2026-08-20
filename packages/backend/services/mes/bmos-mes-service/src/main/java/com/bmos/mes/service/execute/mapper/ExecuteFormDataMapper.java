package com.bmos.mes.service.execute.mapper;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.BooleanUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.execute.dto.FormDataDiscardDTO;
import com.bmos.mes.service.execute.dto.FormDataListQueryDTO;
import com.bmos.mes.service.execute.dto.RecordItemLatestDataQueryDTO;
import com.bmos.mes.service.execute.enums.ExecuteFormDataType;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.FormDataProcedureInfo;
import com.bmos.mes.service.process.dto.query.CalculateDataQueryDTO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mapper
public interface ExecuteFormDataMapper extends BaseMapperX<ExecuteFormData> {

    default List<ExecuteFormData> selectListByCondition(FormDataListQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, dto.getProductPlanId())
                .eq(ExecuteFormData::getProcedureStepId, dto.getReuse() ? 0 : dto.getProcedureStepId())
                .eq(dto.getReuse(), ExecuteFormData::getReuse, dto.getReuse())
                .eq(ExecuteFormData::getCopyVersion, dto.getCopyVersion())
                .eqIfPresent(ExecuteFormData::getFieldId, dto.getFieldId())
                .eqIfPresent(ExecuteFormData::getDiscard, dto.getDiscard())
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    default List<ExecuteFormData> selectByStep(RecordItemLatestDataQueryDTO dto) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, dto.getProductPlanId())
                .eq(ExecuteFormData::getProcedureStepId, dto.getReuse() ? 0 : dto.getProcedureStepId())
                .eq(ExecuteFormData::getRecordItemId, dto.getRecordItemId())
                .eq(ExecuteFormData::getCopyVersion, dto.getCopyVersion())
                .eq(ExecuteFormData::getReuse, dto.getReuse())
                .eqIfPresent(ExecuteFormData::getDiscard, dto.getDiscard())
                .inIfPresent(ExecuteFormData::getFieldId, dto.getFieldIdList())
                .orderByDesc(ExecuteFormData::getFieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    default List<ExecuteFormData> selectByProductPlanIds(List<Long> planIds) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .in(ExecuteFormData::getProductPlanId, planIds)
                .eq(ExecuteFormData::getDiscard, false));
    }

    void discardFields(FormDataDiscardDTO dto);

    List<ExecuteFormData> selectCurrentRecordItemFieldValues(@Param("query") CalculateDataQueryDTO query,
                                                             @Param("fieldIds") Set<Long> fieldIds);

    List<ExecuteFormData> selectOtherRecordItemFieldValues(@Param("query") CalculateDataQueryDTO query,
                                                           @Param("fieldIds") Set<Long> fieldIds);

    default List<ExecuteFormData> selectByProductPlanId(Long productPlanId) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .eq(ExecuteFormData::getDiscard, false));
    }


    default List<ExecuteFormData> selectByProductPlanIdAndItemIds(Long productPlanId, Collection<Long> recordItemIds) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .in(ExecuteFormData::getRecordItemId, recordItemIds)
                .eq(ExecuteFormData::getDiscard, false)
                .orderByDesc(ExecuteFormData::getFieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    default List<ExecuteFormData> selectByProductPlanIdAndItemIdsAndCopyVersions(Long productPlanId, Collection<Long> recordItemIds, Collection<Long> copyVersions, Collection<Long> procedureStepIds) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .in(ExecuteFormData::getRecordItemId, recordItemIds)
                .in(ExecuteFormData::getCopyVersion, copyVersions)
                .in(ExecuteFormData::getProcedureStepId, procedureStepIds)
                .eq(ExecuteFormData::getDiscard, false)
                .orderByDesc(ExecuteFormData::getFieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    /**
     * 根据生产计划id和记录项id列表查询数据
     * 包含作废数据
     * @param productPlanId
     * @param recordItemIds
     * @return
     */
    default List<ExecuteFormData> selectByProductPlanIdAndItemIdsWithDiscard(Long productPlanId, Collection<Long> recordItemIds) {
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .in(ExecuteFormData::getRecordItemId, recordItemIds)
                .orderByDesc(ExecuteFormData::getFieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    default Long getNextRev(Long productPlanId, Collection<Long> fields) {
        Long maxRev = selectMaxRev(productPlanId, new HashSet<>(fields));
        return maxRev == null ? 0 :maxRev + 1;
    }

    Long selectMaxRev(@Param("productPlanId") Long productPlanId, @Param("fields") Set<Long> fields);

    default Boolean existedCurrentStepRecordData(ProcedureStepModel procedureStepModel, Long productPlanId, Long copyVersion,List<Long> fieldIds) {
        return exists(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .eq(ExecuteFormData::getRecordItemId, procedureStepModel.getRecordItemId())
                .eq(ExecuteFormData::getProcessId, procedureStepModel.getProcessId())
                .eq(ExecuteFormData::getProcessVersion, procedureStepModel.getProcessVersion())
                .eq(ExecuteFormData::getProcedureStepId, BooleanUtil.isTrue(procedureStepModel.getReusable()) ? 0 : procedureStepModel.getProcedureStepId())
                .eq(ExecuteFormData::getCopyVersion, copyVersion)
                .eq(ExecuteFormData::getSystemCreate, true)
                .in(ExecuteFormData::getFieldId, fieldIds));
    }

    default Boolean existHistoryData(List<ExecuteFormData> results){
        ExecuteFormData first = CollUtil.getFirst(results);
        return exists(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, first.getProductPlanId())
                .eq(ExecuteFormData::getProcedureStepId, first.getProcedureStepId())
                .eq(ExecuteFormData::getReuse, first.getReuse())
                .eq(ExecuteFormData::getCopyVersion, first.getCopyVersion())
                .eq(ExecuteFormData::getRecordItemId, first.getRecordItemId())
                .in(ExecuteFormData::getFieldId, CollectionUtils.convertList(results, ExecuteFormData::getFieldId)));
    }

    default List<ExecuteFormData> selectExistedFieldIds(List<Long> longs, BusinessDataHandleBaseDTO dto){
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, dto.getProductPlanId())
                .eq(ExecuteFormData::getProcedureStepId,BooleanUtil.isTrue(dto.getReuse()) ? 0 : dto.getProcedureStepId())
                .eq(ExecuteFormData::getReuse, dto.getReuse())
                .eq(ExecuteFormData::getCopyVersion, dto.getCopyVersion())
                .eq(ExecuteFormData::getRecordItemId, dto.getRecordItemId())
                .in(ExecuteFormData::getFieldId, longs)
                .groupBy(ExecuteFormData::getFieldId))
                ;
    }

    default List<ExecuteFormData> selectExistedFieldIds(List<Long> longs, CalculateDataQueryDTO dto){
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, dto.getProductPlanId())
                .eq(ExecuteFormData::getProcedureStepId,BooleanUtil.isTrue(dto.getReuse()) ? 0 : dto.getProcedureStepId())
                .eq(ExecuteFormData::getReuse, dto.getReuse())
                .eq(ExecuteFormData::getCopyVersion, dto.getCopyVersion())
                .eq(ExecuteFormData::getRecordItemId, dto.getRecordItemId())
                .in(ExecuteFormData::getFieldId, longs)
                .groupBy(ExecuteFormData::getFieldId))
                ;
    }

    default Integer countModifyFieldByPlanId(Long productPlanId) {
        List<ExecuteFormData> list = selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .eq(ExecuteFormData::getOperationType, ExecuteFormDataType.MODIFY.getValue())
                .groupBy(ExecuteFormData::getFieldId)
                .groupBy(ExecuteFormData::getProcedureStepId)
                .groupBy(ExecuteFormData::getCopyVersion))
                ;
        return list.size();
    }

    default List<ExecuteFormData> selectByPlanIdsAndFieldId(Collection<Long> planIdList, Long fieldId){
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .in(ExecuteFormData::getProductPlanId, planIdList)
                .eq(ExecuteFormData::getFieldId, fieldId)
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    default List<ExecuteFormData> selectByPlanIdList(List<Long> planIdList){
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .in(ExecuteFormData::getProductPlanId, planIdList)
                .eq(ExecuteFormData::getDiscard, false));
    }

    default List<ExecuteFormData> queryModifyRecordPage(Long productPlanId,
                                                        Collection<Long> procedureStepIds,
                                                        Collection<Long> procedureStepModelIds){
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .eq(ExecuteFormData::getOperationType, ExecuteFormDataType.MODIFY.getValue())
                .and(executeFormDataLambdaQueryWrapper -> {
                    executeFormDataLambdaQueryWrapper.in(ExecuteFormData::getProcedureStepId, procedureStepIds)
                            .or()
                            .in(ExecuteFormData::getProcedureStepModelId, procedureStepModelIds);
                })
                .orderByDesc(ExecuteFormData::getOperationTime));
    }

    default List<ExecuteFormData> selectByStepsAndFieldIds(Long productPlanId, List<Long> stepIdList, List<Long> fieldIds){
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .in(ExecuteFormData::getProcedureStepId, stepIdList)
                .in(ExecuteFormData::getFieldId, fieldIds));
    }

    default List<ExecuteFormData> selectByPlanIdAndItemIdAndStepId(Long productPlanId, Long recordItemId, Long stepId){
        return selectList(new LambdaQueryWrapperX<ExecuteFormData>()
                .eq(ExecuteFormData::getProductPlanId, productPlanId)
                .eq(ExecuteFormData::getRecordItemId, recordItemId)
                .eq(ExecuteFormData::getProcedureStepId, stepId));
    }

    List<FormDataProcedureInfo> selectProcessAndProcedureByFormDataIds(@Param("formDataIds") Collection<Long> formDataIds);
}
