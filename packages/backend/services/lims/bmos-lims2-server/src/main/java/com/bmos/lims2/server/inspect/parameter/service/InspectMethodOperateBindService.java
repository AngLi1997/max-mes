package com.bmos.lims2.server.inspect.parameter.service;

import com.bmos.lims2.server.inspect.parameter.dto.InspectMethodOperateBindBatchSaveDTO;
import com.bmos.lims2.server.inspect.parameter.dto.OperateRuleMethodBindBatchSaveDTO;
import com.bmos.lims2.server.inspect.parameter.dto.MethodBoundOperateEffectiveDTO;

import java.util.List;

/**
 * @Description: 方法-操作规程 绑定服务接口
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
public interface InspectMethodOperateBindService {

    /**
     * 覆盖式保存：为指定方法（recordId）批量绑定操作规程
     */
    void saveOperateBindingsForMethod(InspectMethodOperateBindBatchSaveDTO dto);

    /**
     * 覆盖式保存：为指定操作规程批量绑定方法（recordId）
     */
    void saveMethodBindingsForOperate(OperateRuleMethodBindBatchSaveDTO dto);

    /**
     * 按方法查询绑定的操作规程ID集合
     */
    List<Long> listOperateIdsByMethod(Long recordId);

    /**
     * 按操作规程查询绑定的方法记录ID集合
     */
    List<Long> listMethodRecordIdsByOperate(Long operateId);

    /**
     * 按方法查询：已绑定且有启用版本的操作规程信息（含下载路径）
     */
    List<MethodBoundOperateEffectiveDTO> listEffectiveOperateByMethod(Long recordId);
}


