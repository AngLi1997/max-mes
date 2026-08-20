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
@TableName("bm_process_relation")
@Builder
public class ProcessRelation {
    @Tolerate
    public ProcessRelation(){}

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long processId;

    private Long relationProcessId;
}
