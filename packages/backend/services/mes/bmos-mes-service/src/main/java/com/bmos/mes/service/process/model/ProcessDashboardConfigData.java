package com.bmos.mes.service.process.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/12/31 16:54
 */
@Data
@TableName(value = "bm_process_dashboard_config_data")
public class ProcessDashboardConfigData extends BaseDO {

    /**
     * 工艺看板配置id bm_process_dashboard_config表的主键id
     */
    private Long dashboardConfigId;

    /**
     * 工序id
     */
    private Long procedureId;

    /**
     * 工序名称
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
     * 模型编码
     */
    private String modelCode;

    /**
     * 排序号
     */
    private Integer sort;

}
