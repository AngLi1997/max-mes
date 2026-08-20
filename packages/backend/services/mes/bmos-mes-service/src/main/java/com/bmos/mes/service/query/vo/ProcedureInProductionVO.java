package com.bmos.mes.service.query.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @className: ProcedureInProduction
 * @author: yigaohui
 * @date: 2024/12/4 18:35
 * @Version: 1.0
 * @description:
 */

@ApiModel("生产中工序VO")
@Data
public class ProcedureInProductionVO {

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工艺名称")
    private String processName;

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("生产中的批次批号")
    private List<String> inProductionBatchNoList;
}
