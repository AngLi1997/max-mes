package com.bmos.mes.service.query.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @className: ProductScheduleProcedureConfigVO
 * @author: yigaohui
 * @date: 2024/12/4 11:02
 * @Version: 1.0
 * @description:
 */

@ApiModel("生产进度工序配置VO")
@Data
public class ProductScheduleProcedureConfigVO {
    @ApiModelProperty(value = "工艺id")
    private Long processId;

    @ApiModelProperty("工序名称")
    private String processName;

    @ApiModelProperty("工序id")
    private Long procedureId;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("顺序号")
    private int seq;
}
