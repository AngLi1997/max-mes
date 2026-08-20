package com.bmos.lims2.server.inspect.parameter.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.entity.BatchRecord;
import com.bmos.lims2.server.eln.record.mapper.BatchRecordMapper;
import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodBindBatchSaveDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethod;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectMethodMapper;
import com.bmos.lims2.server.inspect.parameter.service.InspectMethodBindService;
import com.bmos.logging.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 分析项-方法 绑定服务实现（使用 lm_inspect_parameter_record）
 * @Author: yigaohui
 * @Date: 2025/10/31 11:30
 */
@Service
public class InspectMethodBindServiceImpl implements InspectMethodBindService {

    @Autowired
    private InspectMethodMapper inspectMethodMapper;

    @Autowired
    private BatchRecordMapper recordMapper;

    @Override
    @OperationLog
    @Transactional(rollbackFor = Exception.class)
    public void saveBindings(InspectMethodBindBatchSaveDTO dto) {
        Long parameterId = dto.getParameterId();
        List<Long> recordIdList = dto.getRecordIdList();

        // 1) 为空则清理旧绑定后结束
        if (CollUtil.isEmpty(recordIdList)) {
            inspectMethodMapper.clearByParameterId(parameterId);
            return;
        }

        // 2) 去重
        LinkedHashSet<Long> distinctIds = new LinkedHashSet<>(recordIdList);

        // 4) 校验这些方法是否已经绑定到其他分析项（一个方法只能绑定到一个分析项）
        java.util.List<InspectMethod> existed = inspectMethodMapper.selectByRecordIdList(new java.util.ArrayList<>(distinctIds));
        java.util.List<Long> conflictMethodIds = new java.util.ArrayList<>();
        for (InspectMethod em : existed) {
            if (em.getParameterId() != null && !em.getParameterId().equals(parameterId)) {
                conflictMethodIds.add(em.getRecordId());
            }
        }
        if (!conflictMethodIds.isEmpty()) {
            List<BatchRecord> batchRecords = recordMapper.selectBatchIds(conflictMethodIds);
            throw new BmosException(LimsResponseCode.METHOD_ALREADY_BOUND, batchRecords.stream().map(BatchRecord::getName).collect(Collectors.joining(",")));
        }

        // 5) 清理旧绑定，再批量绑定（将给定方法ID的 parameter_id 设置为当前分析项ID）
        inspectMethodMapper.clearByParameterId(parameterId);
        List<InspectMethod> list = new ArrayList<>(distinctIds.size());
        for (Long recordId: distinctIds) {
            InspectMethod m = new InspectMethod();
            m.setRecordId(recordId);
            m.setParameterId(parameterId);
            list.add(m);
        }
        inspectMethodMapper.saveBatch(list);
    }
}


