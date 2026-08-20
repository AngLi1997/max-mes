package com.bmos.mes.service.process.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.process.AuditPerorationStateEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.process.convert.ProcessConfirmConverter;
import com.bmos.mes.service.process.dto.ConfirmUpdateDTO;
import com.bmos.mes.service.process.dto.query.ProcedureConfirmQueryDTO;
import com.bmos.mes.service.process.dto.save.ProcedureConfirmSaveDTO;
import com.bmos.mes.service.process.mapper.ProcedureConfirmMapper;
import com.bmos.mes.service.process.model.ProcedureConfirm;
import com.bmos.mes.service.process.service.ProcedureConfirmService;
import com.bmos.mes.service.process.vo.ProcedureConfirmVO;
import com.bmos.mybatis.page.CommonPage;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class ProcedureConfirmServiceImpl implements ProcedureConfirmService {

    @Autowired
    private ProcedureConfirmMapper confirmMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveProcedureConfirm(ProcedureConfirmSaveDTO dto) {
        ProcedureConfirm procedureConfirm = ProcessConfirmConverter.INSTANCE.convertToProcedureConfirm(dto);
        confirmMapper.saveProcedureConfirm(procedureConfirm);
    }

    @Override
    public CommonPage<ProcedureConfirmVO> queryProcedurePageByProcessId(ProcedureConfirmQueryDTO dto) {
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize(), dto.getOrderSql());
        List<ProcedureConfirm> list = confirmMapper.queryProcedurePageByProcessId(dto.getProcessConfirmId());
        return CommonPage.convertPage(ProcessConfirmConverter.INSTANCE.convertToProcedureVo(list));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateProcedureById(ConfirmUpdateDTO dto) {
        ProcedureConfirm procedureConfirm = confirmMapper.queryOneById(dto.getId());
        if (ObjectUtil.isEmpty(procedureConfirm)){
            throw new BmosException(MesResponseCode.PROCESS_DATE_ERROR);
        }
        procedureConfirm.setConfirmOpinion(AuditPerorationStateEnum.valueOf(dto.getOpinion()));
        procedureConfirm.setRemark(dto.getRemark());
        procedureConfirm.setConfirmTime(LocalDateTime.now());
        return confirmMapper.saveProcedureConfirm(procedureConfirm);
    }

    @Override
    public Set<String> queryProcedureNameByProcessId(Long processId) {
        List<ProcedureConfirm> procedureConfirms = confirmMapper.queryProcedureNameByProcessId(processId);
        if (CollUtil.isEmpty(procedureConfirms)){
            return Collections.emptySet();
        }
        return CollectionUtils.convertSet(procedureConfirms,ProcedureConfirm::getProcedureName);
    }
}
