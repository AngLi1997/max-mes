package com.bmos.lims2.server.eln.record.service;

import com.bmos.lims2.server.eln.record.dto.ParameterBindSaveDTO;

import java.util.List;

/**
 * @Description: 批记录-分析项绑定服务
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
public interface BatchRecordParameterBindService {

    /**
     * 保存绑定（覆盖式：先清旧后存新）
     */
    void saveBindings(ParameterBindSaveDTO dto);

    /**
     * 根据记录ID查询已绑定的分析项ID列表
     */
    List<Long> getBoundParameterIds(Long recordId);
}


