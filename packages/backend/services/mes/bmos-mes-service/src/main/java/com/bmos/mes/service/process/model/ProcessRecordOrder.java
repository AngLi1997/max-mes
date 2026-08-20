package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

@Getter
@Setter
@ToString
@TableName("bm_process_record_order")
@Builder
public class ProcessRecordOrder {

    @Tolerate
    public ProcessRecordOrder(){}

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long recordItemId;

    private Long recordVersionId;

    private Long recordItemOrder;

    private Long processId;

    private Long processVersionId;

    private String processVersion;

    private Boolean reusable;

    /**
     * 工序步骤模型id
     */
    private Long procedureStepModelId;
}
