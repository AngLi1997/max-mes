package com.bmos.lims2.server.inspect.parameter.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 分析项-方法 批量绑定保存DTO
 * @Author: yigaohui
 * @Date: 2025/10/31 11:30
 */
@Getter
@Setter
public class InspectMethodBindBatchSaveDTO {

    /** 分析项ID */
    @NotNull(message = "分析项ID不能为空")
    private Long parameterId;

    /**
     * 方法ID列表（lm_inspect_parameter_record 的主键ID），可为空；为空表示解除绑定
     */
    private List<Long> recordIdList;
}

