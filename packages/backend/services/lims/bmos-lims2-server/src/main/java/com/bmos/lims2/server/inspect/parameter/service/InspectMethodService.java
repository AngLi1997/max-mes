package com.bmos.lims2.server.inspect.parameter.service;

import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodEffectiveDTO;
import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodSaveDTO;
import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodTreeNodeDTO;
import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodUpdateDTO;
import com.bmos.lims2.server.inspect.parameter.entity.InspectMethod;

import java.util.List;

/**
 * @Description: 分析方法Service接口
 * @Author: yigaohui
 * @Date: 2025/10/27 00:00
 */
public interface InspectMethodService {

    /**
     * 按分析项ID查询方法列表
     */
    List<InspectMethod> listByParameterId(Long parameterId);

    /**
     * 按分析项ID查询【有生效版本】的方法列表
     * @param parameterId 分析项ID
     * @return 方法及其生效版本信息
     */
    List<InspectMethodEffectiveDTO> listEffectiveMethodsByParameterId(Long parameterId);
}


