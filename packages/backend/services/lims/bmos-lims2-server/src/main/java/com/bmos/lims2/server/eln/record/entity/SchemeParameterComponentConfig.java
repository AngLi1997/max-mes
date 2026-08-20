package com.bmos.lims2.server.eln.record.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 工序步骤记录项配置实体
 */
@Getter
@Setter
@ToString
@TableName("lm_scheme_parameter_component_config")
public class SchemeParameterComponentConfig extends BaseDO {

    /**
     * 工序步骤id
     * 实际为procedure_step_model的Id
     */
    private Long parameterId;

    /**
     * 工序步骤模型id
     */
    private Long parameterConfigId;

    /**
     * 工艺id
     */
    private Long schemeId;

    /**
     * 工艺版本号
     */
    private String schemeVersionId;

    /**
     * 记录项id
     */
    private Long recordItemId;

    /**
     * 记录项版本id
     */
    private Long recordVersionId;

    /**
     * 配置信息JSON
     */
    private String configInfo;

    /**
     * 组件id
     */
    private Long componentId;

    /**
     * field_id
     */
    private Long fieldId;

}
