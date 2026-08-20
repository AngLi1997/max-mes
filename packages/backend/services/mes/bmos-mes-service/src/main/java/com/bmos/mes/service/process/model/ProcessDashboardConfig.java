package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mes.service.process.vo.ProcessDashboardProcedureVO;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 工艺看板配置
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 17:03
 */
@TableName(value = "bm_process_dashboard_config", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ProcessDashboardConfig extends BaseDO {

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
    @TableField(value = "procedure_list", typeHandler = JacksonTypeHandler.class)
    private List<ProcessDashboardProcedureVO> procedureList;

    public ProcessDashboardConfig(Process process, List<ProcessDashboardProcedureVO> procedureList){
        this.processId = process.getId();
        this.processName = process.getName();
        this.processVersion = process.getActiveVersion();
        this.procedureList = procedureList;
    }
}
