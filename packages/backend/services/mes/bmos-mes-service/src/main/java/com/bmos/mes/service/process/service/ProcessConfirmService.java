package com.bmos.mes.service.process.service;

import com.bmos.mes.service.platform.dict.vo.DictVO;
import com.bmos.mes.service.process.dto.ConfirmUpdateDTO;
import com.bmos.mes.service.process.dto.query.AuditOpinionQueryDTO;
import com.bmos.mes.service.process.dto.query.ProcessConfirmQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcessConfirmSaveDTO;
import com.bmos.mes.service.process.model.ProcessConfirm;
import com.bmos.mes.service.process.vo.AuditOpinionVO;
import com.bmos.mes.service.process.vo.ProcessConfirmVO;
import com.bmos.mes.service.process.vo.StatisticsVO;
import com.bmos.mybatis.page.CommonPage;

import java.util.List;
import java.util.Set;

public interface ProcessConfirmService {

    void saveProcessConfirm(ProcessConfirmSaveDTO saveDTO);

    ProcessConfirm queryProcessConfirmByInstanceId(String instanceId);

    CommonPage<ProcessConfirmVO> getProcessConfirmPageList(ProcessConfirmQueryDTO dto);

    Set<DictVO> getProcessNameList();

    Boolean updateProcessOpinion(ConfirmUpdateDTO dto);

    CommonPage<AuditOpinionVO> listProcessOpinionPage(AuditOpinionQueryDTO dto);

    List<StatisticsVO> processStatistics(AuditOpinionQueryDTO dto);
}
