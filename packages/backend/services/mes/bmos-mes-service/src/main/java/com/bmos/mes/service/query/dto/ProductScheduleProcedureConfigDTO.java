package com.bmos.mes.service.query.dto;

import io.swagger.annotations.ApiModel;
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
public class ProductScheduleProcedureConfigDTO {

    private Long processId;

    private String processName;
    private Long procedureId;
    private String procedureName;

    private int seq;
}
