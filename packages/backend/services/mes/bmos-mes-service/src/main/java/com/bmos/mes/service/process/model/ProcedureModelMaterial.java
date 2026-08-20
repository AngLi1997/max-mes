package com.bmos.mes.service.process.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("bm_procedure_model_material")
@Data
public class ProcedureModelMaterial {

    /**
     * 工序 model id
     */
    private Long procedureModelId;

    /**
     * 产品配方物料id
     */
    private Long productFormulaMaterialId;

}
