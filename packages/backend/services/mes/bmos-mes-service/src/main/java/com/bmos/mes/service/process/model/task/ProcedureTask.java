package com.bmos.mes.service.process.model.task;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@TableName(value = "bm_procedure_task")
public class ProcedureTask extends BaseDO {

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺版本")
    private String processVersion;

    @ApiModelProperty("任务名称")
    private String name;

    @ApiModelProperty("工序模型id")
    private Long procedureModelId;


}
