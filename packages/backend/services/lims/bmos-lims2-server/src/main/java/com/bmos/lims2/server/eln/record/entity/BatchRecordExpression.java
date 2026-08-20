package com.bmos.lims2.server.eln.record.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 批记录公式绑定关系
 */
@Getter
@Setter
@ToString
    @TableName(value = "bm_batch_record_expression")
public class BatchRecordExpression {

    @ApiModelProperty(value = "批记录id")
    private Long recordId;

    @ApiModelProperty(value = "公式id")
    private Long expressionId;
}
