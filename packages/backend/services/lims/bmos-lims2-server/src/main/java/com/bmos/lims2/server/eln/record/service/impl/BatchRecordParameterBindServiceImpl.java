package com.bmos.lims2.server.eln.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.record.dto.ParameterBindSaveDTO;
import com.bmos.lims2.server.eln.record.service.BatchRecordParameterBindService;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethod;
import com.bmos.lims2.server.inspect.parameter.mapper.InspectMethodMapper;
import com.bmos.logging.annotation.OperationLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 批记录-分析项绑定服务实现（使用 lm_inspect_parameter_record）
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
@Service
public class BatchRecordParameterBindServiceImpl implements BatchRecordParameterBindService {

    @Autowired
    private InspectMethodMapper inspectMethodMapper;

    @Override
    @OperationLog
    @Transactional(rollbackFor = Exception.class)
    public void saveBindings(ParameterBindSaveDTO dto) {
        List<Long> parameterIdList = dto.getParameterIdList();

        // 一个方法（recordId）只能绑定到一个分析项：此接口为“方法绑定分析项（覆盖式）”，因此仅允许传入最多1个分析项
        if (CollUtil.isNotEmpty(parameterIdList) && parameterIdList.size() > 1) {
            throw new BmosException(LimsResponseCode.INVALID_PARAM, "同一方法仅能绑定一个分析项");
        }

        inspectMethodMapper.deleteByRecordId(dto.getRecordId());
        if (CollUtil.isEmpty(parameterIdList)) {
            return;
        }

        List<InspectMethod> list = new ArrayList<>(parameterIdList.size());
        for (int i = 0; i < parameterIdList.size(); i++) {
            InspectMethod m = new InspectMethod();
            m.setRecordId(dto.getRecordId());
            m.setParameterId(parameterIdList.get(i));
            list.add(m);
        }
        inspectMethodMapper.saveBatch(list);
    }

    @Override
    public List<Long> getBoundParameterIds(Long recordId) {
        List<InspectMethod> list = inspectMethodMapper.selectByRecordIdList(java.util.Collections.singletonList(recordId));
        return CollectionUtils.convertList(list, InspectMethod::getParameterId);
    }
}


