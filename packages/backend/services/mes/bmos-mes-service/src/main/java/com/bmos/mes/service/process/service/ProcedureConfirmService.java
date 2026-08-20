package com.bmos.mes.service.process.service;

import com.bmos.mes.service.process.dto.ConfirmUpdateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureConfirmQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcedureConfirmSaveDTO;
import com.bmos.mes.service.process.vo.ProcedureConfirmVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.Set;

public interface ProcedureConfirmService {

    void saveProcedureConfirm(ProcedureConfirmSaveDTO dto);

    CommonPage<ProcedureConfirmVO> queryProcedurePageByProcessId(ProcedureConfirmQueryDTO dto);

    Boolean updateProcedureById(ConfirmUpdateDTO dto);

    Set<String> queryProcedureNameByProcessId(Long processId);
}
