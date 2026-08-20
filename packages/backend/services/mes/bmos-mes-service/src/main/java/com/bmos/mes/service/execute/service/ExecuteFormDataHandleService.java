package com.bmos.mes.service.execute.service;

import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.preparation.measure.dto.FormDataFilterDTO;

import java.util.List;

public interface ExecuteFormDataHandleService {


    /**
     * 1.填充表单数据 - 班组、操作类型
     * 2.过滤出数据库中不存在或者值有变更的数据
     * 3.处理时间日期格式
     * 注意: 生产计划id与copyVersion取数据列表第一个executeFormData的值
     * @param build
     * @return
     */
    List<ExecuteFormData> fillFormDataAndFilter(FormDataFilterDTO build);

}
