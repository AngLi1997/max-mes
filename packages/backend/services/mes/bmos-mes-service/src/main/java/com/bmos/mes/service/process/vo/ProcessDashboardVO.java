package com.bmos.mes.service.process.vo;

import com.bmos.mes.service.process.convert.ProcessDashboardConfigConverter;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.Process;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 工艺看板配置
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 16:54
 */
@Data
@NoArgsConstructor
public class ProcessDashboardVO {

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 工艺版本
     */
    private String processVersion;

    /**
     * 工步列表
     */
    private List<ProcessDashboardProcedureVO> procedureList;


    public ProcessDashboardVO(Process process, List<ProcedureModel> procedureModels) {
        this.processId = process.getId();
        this.processName = process.getName();
        this.processVersion = process.getActiveVersion();
        this.setProcedureList(ProcessDashboardConfigConverter.INSTANCE.convertTOProcedureModelList(procedureModels));
    }
}
