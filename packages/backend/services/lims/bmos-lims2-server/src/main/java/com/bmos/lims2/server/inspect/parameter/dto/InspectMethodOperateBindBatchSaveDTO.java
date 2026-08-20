package com.bmos.lims2.server.inspect.parameter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 按方法（recordId）批量绑定操作规程 入参DTO
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Getter
@Setter
public class InspectMethodOperateBindBatchSaveDTO {

    @ApiModelProperty("方法记录ID -> bm_batch_record.id")
    private Long recordId;

    @ApiModelProperty("操作规程ID集合 -> bm_operate_rule.id")
    private List<Long> operateIdList;
}


