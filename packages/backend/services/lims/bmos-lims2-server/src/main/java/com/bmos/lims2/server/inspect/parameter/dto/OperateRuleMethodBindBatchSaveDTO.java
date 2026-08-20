package com.bmos.lims2.server.inspect.parameter.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @Description: 按操作规程批量绑定方法（recordId） 入参DTO
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Getter
@Setter
public class OperateRuleMethodBindBatchSaveDTO {

    @ApiModelProperty("操作规程ID -> bm_operate_rule.id")
    private Long operateId;

    @ApiModelProperty("方法记录ID集合 -> bm_batch_record.id")
    private List<Long> recordIdList;
}


