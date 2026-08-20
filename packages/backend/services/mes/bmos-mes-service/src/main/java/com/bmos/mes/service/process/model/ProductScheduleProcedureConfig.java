package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @className: ProductScheduleProcedureConfig
 * @author: yigaohui
 * @date: 2024/12/4 11:33
 * @Version: 1.0
 * @description:
 */

@TableName("product_schedule_procedure_config")
@Data
public class ProductScheduleProcedureConfig {

    private Long id;

    private Long processId;

    private Long procedureId;

    private int seq;
}
