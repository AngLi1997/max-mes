package com.bmos.mes.service.execute.vo;

import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/2/7 17:10
 */
@Data
public class FormDataProcedureInfo {

    /**
     * 表单数据id
     */
    private Long formDataId;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 工序名称
     */
    private String procedureName;
}
