package com.bmos.mes.service.plan.document.service.impl;

import com.bmos.common.base.user.SysUser;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.service.plan.document.controller.vo.TemplateVersionHistoryVO;
import com.bmos.mes.service.plan.document.convert.BatchTemplateConverter;
import com.bmos.mes.service.plan.document.convert.BatchTemplateLogConverter;
import com.bmos.mes.service.plan.document.mapper.BatchTemplateOperateLogMapper;
import com.bmos.mes.service.plan.document.model.BatchTemplateOperateLog;
import com.bmos.mes.service.plan.document.service.BatchTemplateLogService;
import com.bmos.mes.service.plan.document.service.dto.BatchTemplateLogSaveDTO;
import com.bmos.mes.service.plan.document.service.dto.TemplateVersionOperateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 批记录模板日志
 */
@Service
public class BatchTemplateLogServiceImpl implements BatchTemplateLogService {

    @Autowired
    private BatchTemplateOperateLogMapper batchTemplateOperateLogMapper;


    @Override
    public void saveTemplateLog(BatchTemplateLogSaveDTO dto) {
        SysUser user = SysUserHolder.getUser();
        BatchTemplateOperateLog convert = BatchTemplateLogConverter.INSTANCE.convert(dto, user);
        batchTemplateOperateLogMapper.insert(convert);
    }

    @Override
    public List<TemplateVersionHistoryVO> templateVersionHistory(TemplateVersionOperateDTO dto) {
        List<BatchTemplateOperateLog> batchTemplateOperateLogs = batchTemplateOperateLogMapper.selectByVersionId(dto.getTemplateVersionId());
        return BatchTemplateConverter.INSTANCE.convert2LogVO(batchTemplateOperateLogs);
    }
}
