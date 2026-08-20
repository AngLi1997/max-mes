package com.bmos.lims2.server.eln.entry.dto;

import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
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
    private Long parameterConfigId;
}
