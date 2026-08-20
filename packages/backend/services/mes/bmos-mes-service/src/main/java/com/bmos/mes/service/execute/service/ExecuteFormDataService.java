package com.bmos.mes.service.execute.service;

import com.bmos.mes.service.execute.dto.*;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.*;
import com.bmos.mes.service.process.dto.query.CalculateDataQueryDTO;
import com.bmos.mes.service.utils.DateCalculateVO;
import com.bmos.mybatis.page.CommonPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ExecuteFormDataService {

    void saveBatch(FormDataBatchSaveDTO dto);

    void modify(FormDataModifyDTO dto);

    List<ExecuteFormData> calculateData(List<ExecuteFormData> saveData, CalculateDataQueryDTO query);

    List<FormDataVO> getFieldList(FormDataListQueryDTO dto);

    void lockProcedureStep(LockStepDTO dto);

    void unLockProcedureStep(LockStepDTO dto);

    List<FormDataItemVO> getRecordItemLatestData(RecordItemLatestDataQueryDTO dto);

    Long copyRecordItem(RecordCopySaveDTO dto);

    List<CopyRecordItemVO> getCopyVersionList(RecordCopyQueryDTO dto);

    void discardRecordItem(FormDataDiscardDTO dto);

    List<IntactFormDataVO> getIntactMergedList(IntactMergeListQueryDTO dto);

    String getServerTime();

    void saveBusinessComponentsData(BusinessComponentBatchSaveDTO dto);

    Boolean checkBusinessComponentsSaved(BusinessComponentsCheckSavedDTO dto);

    void insertBatch(List<ExecuteFormData> results);

    Boolean existHistoryData(List<ExecuteFormData> results);

    Long selectMaxRev(Long productPlanId, Set<Long> fields);

    /**
     * 保存业务组件结果数据并处理关联公式组件数据
     * @param results 业务组件结果集
     * @param dto
     */
    void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, BusinessDataHandleBaseDTO dto);

    void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, BusinessDataHandleBaseDTO dto, boolean filterNull);

    /**
     * @param results 需要保存的业务组件批记录结果
     * @param productPlanId 生产计划id
     * @param procedureStepModelId 工步模型id
     * @param copyVersion 记录拷贝版本
     */
    void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, Long productPlanId,
                                                   Long procedureStepModelId, Long copyVersion);

    void saveResultsAndHandleRelationComponentData(List<ExecuteFormData> results, Long productPlanId,
                                                   Long procedureStepModelId, Long copyVersion, boolean filterNull);

    /**
     * 保存业务组件数据并计算关联公式数据且保存
     * 同时处理异常值自动记录
     * @param results 业务组件结果
     * @param productPlanId 生产计划id
     * @param componentId 业务组件顶层id
     * @param procedureStepModelId 业务组件所处工步模型id
     * @param copyVersion 复制版本
     */
    void saveResultsAndHandleRelationWithExceptionRecord(List<ExecuteFormData> results,
                                                         Long productPlanId,
                                                         Long componentId,
                                                         Long procedureStepModelId,
                                                         Long copyVersion);

    /**
     * 通过计划id和记录id集合获取数据
     *
     * @param planId        计划id
     * @param recordItemIds 记录项id集合
     * @return 获取结果
     */
    List<ExecuteFormData> getDataByPlanAndItemIds(Long planId, Collection<Long> recordItemIds);

    DateCalculateVO getCalculateDate(CalculateDateDTO dto);

    /**
     * 更新组件数据值 操作记录为更新
     * 提供给 签名组件的继续签名操作使用
     * @param dto
     */
    void update(FormDataUpdateDTO dto);

    List<ExecuteFormData> selectByProductPlanIdAndItemIds(Long productPlanId, List<Long> recordItemIds);

    List<ExecuteFormData> selectByProductPlanIdAndItemIdsAndCopyVersions(Long productPlanId, Collection<Long> recordItemIds, Collection<Long> copyVersions, Collection<Long> procedureStepIds);

    AttachmentVO upload(MultipartFile file);

    String pictureList(String value);

    /**
     * 对一个数值组件进行趋势分析
     *
     * @param dto
     * @return
     */
    TrendAnalysisVO componentTrendAnalysis(ComponentTrendAnalysisDTO dto);

    /**
     * 统计更新生产计划修订数量
     * 并处理生产信息组件-修订数量的值
     * @param productPlanId
     */
    void handlePlanModifyCount(Long productPlanId);

    /**
     * 根据批次id查询表单数据
     *
     * @param planIdList
     * @return
     */
    List<ExecuteFormData> selectByPlanIdList(List<Long> planIdList);

    /**
     * 查看工序 根据工序模型id与生产计划id查询该工序下步骤/任务列表及记录项
     * @param dto
     * @return
     */
    List<ProcedureViewVO> queryProcedureViewVO(ProcedureViewQueryDTO dto);

    /**
     * 查询已存在的复制版本列表 区别于getCopyVersionList
     * 在不存在时不会创建版本为0的记录页
     * @param dto
     * @return
     */
    List<CopyRecordItemVO> getExistedCopyVersionList(RecordCopyQueryDTO dto);

    /**
     * 查询生产计划组件修订记录
     * @param dto
     * @return
     */
    CommonPage<PlanFieldModifyVO> queryPlanModifyList(PlanFieldModifyQueryDTO dto);

    /**
     * 根据辅助记录id查询辅助记录归档html
     * @param id
     * @return
     */
    List<SubsidiaryRecordDocVO> getSubsidiaryDocList(Long id);

    /**
     * 获取公式计算预览结果
     * @param dto
     * @return
     */
    List<FormDataItemVO> getCalculationPreview(FormDataBatchSaveDTO dto);

    /**
     * 根据formDataId查询工序和工序步骤信息
     * @param formDataIds
     * @return
     */
    List<FormDataProcedureInfo> selectProcessAndProcedureByFormDataIds(@Param("formDataIds") Collection<Long> formDataIds);
}
