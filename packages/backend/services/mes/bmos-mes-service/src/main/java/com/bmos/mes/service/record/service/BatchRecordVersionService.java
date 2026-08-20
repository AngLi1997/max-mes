package com.bmos.mes.service.record.service;

import com.bmos.mes.service.platform.expression.vo.ExpressionTreeNodeVO;
import com.bmos.mes.service.record.dto.*;
import com.bmos.mes.service.record.model.BatchRecordVersion;
import com.bmos.mes.service.record.vo.*;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface BatchRecordVersionService {


    BatchRecordVersion saveOrUpdateVersion(BatchRecordSaveDTO dto);

    Boolean updateVersion(BatchRecordVersion convertCopyToDo);

    Long copyVersion(CopyVersionDTO dto);

    List<RecordVersionVO> listVersion(Long recordId);

    List<RecordVersionVO> listPorductRecord(Long productId, Long recordId);

    List<VersionLogVO> listRecordLog(Long versionId);

    Boolean checkoutSaveRecord(Long recordId);

    BatchRecordVersion queryById(Long recordVersionId);

    CommonPage<PageRecordAuditVO> pageRecordAudit(RecordAuditDTO dto);

    Boolean startFlow(Long versionId);

    void auditRecordSuccessCallBack(String processInstanceId,String comment,String userId);

    void auditRecordRejectCallBack(String processInstanceId,String comment,String remark,String nodeName,String userId);

    List<SelectRecorVO> queryRecordVersionByRecordId(Long recordId);

    void auditRecordExecutionSuccessCallBack(String businessKey,String remark,String userId,String nodeName,String comment);

    List<String> getAuditBusinessKey(List<Long> deptIdList);

    List<BatchRecordVersion> queryVersionByRecordIdList(List<Long> recordIdList);

    /**
     * 获取平台公式及内置公式
     * @return
     */
    List<ExpressionTreeNodeVO> queryPlatformExpressionAndBuiltInFunction(ExpressionQueryDTO dto);

    /**
     * 获取记录名称及
     * @param recordVersionId
     * @return
     */
    RecordInfoItemListVO getRecordInfoAndItemList(Long recordVersionId);

    /**
     * 修改记录项顺序
     * @param dto
     */
    void changeRecordItemSort(RecordItemSortUpdateDTO dto);


    List<SelectRecorVO> queryProcessRecordVersionByRecordId(Long recordId, Long processVersionId);
    /**
     * 公式配置-计算预览
     * @param dto
     * @return
     */
    String getFunctionCalculatePreview(FunctionCalculatePreviewDTO dto);

    BatchRecordVersion selectById(Long businessId);

    /**
     * 根据工艺id和记录id列表查询版本
     * @param dto
     * @return
     */
    List<ProcessRecordListVO> queryProcessRecordVersionByRecordIdList(ProcessRecordVersionQueryDTO dto);
}
