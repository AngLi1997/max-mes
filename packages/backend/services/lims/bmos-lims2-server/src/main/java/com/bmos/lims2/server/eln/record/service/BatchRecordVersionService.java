package com.bmos.lims2.server.eln.record.service;


import com.bmos.lims2.server.eln.record.dto.*;
import com.bmos.lims2.server.eln.record.entity.BatchRecordVersion;
import com.bmos.lims2.server.eln.record.vo.*;
import com.bmos.lims2.server.platform.expression.vo.ExpressionTreeNodeVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface BatchRecordVersionService {


    BatchRecordVersion saveOrUpdateVersion(BatchRecordSaveDTO dto);

    Boolean updateVersion(BatchRecordVersion convertCopyToDo);

    Long copyVersion(CopyVersionDTO dto);

    List<RecordVersionVO> listVersion(Long recordId);


    /**
     * 根据分析项id查询批记录版本列表
     */
    List<RecordVersionVO> listParameterRecord(Long parameterId, Long recordId);

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

    /**
     * 公式配置-计算预览
     * @param dto
     * @return
     */
    String getFunctionCalculatePreview(FunctionCalculatePreviewDTO dto);

    BatchRecordVersion selectById(Long businessId);
}
