package com.bmos.mes.service.process.model;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("bm_procedure_model_room")
@Data
public class ProcedureModelRoom {

    /**
     * 工序 model id
     */
    private Long procedureModelId;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 产线房间path
     */
    private String roomIdPath;

}
