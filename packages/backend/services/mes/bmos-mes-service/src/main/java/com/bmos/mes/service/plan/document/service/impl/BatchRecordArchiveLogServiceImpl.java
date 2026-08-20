package com.bmos.mes.service.plan.document.service.impl;

import com.bmos.common.base.user.SysUser;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.mes.service.plan.document.controller.vo.ArchiveHistoryVO;
import com.bmos.mes.service.plan.document.convert.BatchRecordArchiveConverter;
import com.bmos.mes.service.plan.document.mapper.BatchRecordArchiveLogMapper;
import com.bmos.mes.service.plan.document.model.BatchRecordArchiveLog;
import com.bmos.mes.service.plan.document.service.BatchRecordArchiveLogService;
import com.bmos.mes.service.plan.document.service.dto.ArchiveSaveLogDTO;
import com.bmos.mes.service.plan.document.service.dto.BatchRecordArchiveOperateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BatchRecordArchiveLogServiceImpl implements BatchRecordArchiveLogService {

    @Autowired
    private BatchRecordArchiveLogMapper batchRecordArchiveLogMapper;

    @Override
    public void saveLog(List<ArchiveSaveLogDTO> saveLogDTOList) {
        List<BatchRecordArchiveLog> batchRecordArchiveLogList = BatchRecordArchiveConverter.INSTANCE.convert2DOList(saveLogDTOList);
        SysUser user = SysUserHolder.getUser();
        for (BatchRecordArchiveLog batchRecordArchiveLog : batchRecordArchiveLogList) {
            batchRecordArchiveLog.setOperatorId(user.getUserId());
            batchRecordArchiveLog.setOperatorName(user.getUserName());
            batchRecordArchiveLog.setOperatorLoginName(user.getLoginName());
            batchRecordArchiveLog.setOperateTime(LocalDateTime.now());
        }
        batchRecordArchiveLogMapper.insertBatch(batchRecordArchiveLogList);
    }

    @Override
    public List<ArchiveHistoryVO> archiveHistoryList(BatchRecordArchiveOperateDTO dto) {
        List<BatchRecordArchiveLog> batchRecordArchiveLog = batchRecordArchiveLogMapper.selectByArchiveId(dto.getArchiveId());
        return BatchRecordArchiveConverter.INSTANCE.convert2VOList(batchRecordArchiveLog);
    }
}
