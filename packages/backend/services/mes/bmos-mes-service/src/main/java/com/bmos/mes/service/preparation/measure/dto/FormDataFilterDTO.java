package com.bmos.mes.service.preparation.measure.dto;

import com.bmos.mes.service.execute.model.ExecuteFormData;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FormDataFilterDTO {

    /**
     * 数据值
     */
    private List<ExecuteFormData> dataList;

    /**
     * 工步模型id
     */
    private Long procedureStepModelId;
}
