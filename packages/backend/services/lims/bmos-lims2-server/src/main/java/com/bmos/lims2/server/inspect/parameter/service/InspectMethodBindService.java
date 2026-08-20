package com.bmos.lims2.server.inspect.parameter.service;

import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodBindBatchSaveDTO;

/**
 * @Description: 分析项-方法 绑定服务接口
 * @Author: yigaohui
 * @Date: 2025/10/31 11:30
 */
public interface InspectMethodBindService {

    /**
     * 通过分析项ID批量绑定方法（覆盖式：先清旧后存新）
     * @param dto 参数（parameterId + recordIdList）
     */
    void saveBindings(InspectMethodBindBatchSaveDTO dto);
}


