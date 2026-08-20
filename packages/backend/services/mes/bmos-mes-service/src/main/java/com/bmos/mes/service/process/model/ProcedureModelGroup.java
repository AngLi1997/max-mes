package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@TableName("bm_procedure_model_group")
public class ProcedureModelGroup {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long procedureModelId;

    private Long groupId;
}
