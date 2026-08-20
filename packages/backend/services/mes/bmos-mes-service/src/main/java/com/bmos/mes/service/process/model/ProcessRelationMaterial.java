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
@TableName("bm_process_relation_material")
@Builder
public class ProcessRelationMaterial {

    @Tolerate
    public ProcessRelationMaterial(){}


    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long processRelationId;

    private Long materialId;

    private Long processId;

}
