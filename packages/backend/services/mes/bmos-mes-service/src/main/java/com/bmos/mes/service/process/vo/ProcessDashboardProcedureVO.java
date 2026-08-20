package com.bmos.mes.service.process.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 16:54
 */
@Data
@NoArgsConstructor
public class ProcessDashboardProcedureVO {

    /**
     * 工步id
     */
    private Long procedureId;

    /**
     * 工步名称
     */
    private String procedureName;

    /**
     * 自定义名称
     */
    private String customName;

    /**
     * 是否生效
     */
    private Boolean effect = false;

    /**
     * 工序编码
     */
    private String modelCode;

    /**
     * 排序号
     */
    private Integer sort;

    public ProcessDashboardProcedureVO(Long procedureId, String procedureName){
        this.procedureId = procedureId;
        this.procedureName = procedureName;
        this.customName = procedureName;
    }
}
