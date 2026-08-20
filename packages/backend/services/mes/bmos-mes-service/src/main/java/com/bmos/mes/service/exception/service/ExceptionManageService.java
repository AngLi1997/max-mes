package com.bmos.mes.service.exception.service;

import com.bmos.mes.service.exception.dto.*;
import com.bmos.mes.service.exception.model.ExecuteException;
import com.bmos.mes.service.exception.vo.ExceptionPageVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;

public interface ExceptionManageService {

    /**
     * 手动录入异常
     * @param dto 
     */
    void manualRecordException(ExceptionManualRecordDTO dto);

    /**
     * 编辑异常
     * @param dto
     */
    void editException(ExceptionEditDTO dto);

    /**
     * 作废异常
     * @param dto
     */
    void cancelException(ExceptionCancelDTO dto);

    /**
     * 处理异常
     * @param dto
     */
    void handleException(ExceptionHandleDTO dto);

    /**
     * 重新调查
     * @param dto
     */
    void reInvestigateException(ExceptionReInvestigateDTO dto);

    /**
     * 分页查询
     * @param dto
     * @return
     */
    CommonPage<ExceptionPageVO> queryExceptionPage(ExceptionPageQueryDTO dto);

    void saveBatch(List<ExecuteException> list);

    /**
     * 获取批次异常信息分页
     * @param dto
     * @return
     */
    CommonPage<ExceptionPageVO> getBatchExceptionPage(BatchExceptionQueryDTO dto);

    /**
     * 录入修订异常
     * @param recordModifyExceptionDTO
     */
    void recordModifyException(RecordModifyExceptionDTO recordModifyExceptionDTO);

}
