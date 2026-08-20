package com.bmos.mes.service.record.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mes.service.record.model.formula.ComponentFormulaConfig;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 组件详情
 * 保存组件大字段相关信息
 * 该表主键id对应 bm_batch_record_component主键id
 */
@Setter
@Getter
@ToString
@TableName(value = "bm_batch_record_component_detail", autoResultMap = true)
public class BatchRecordComponentDetail extends BaseDO {

    /**
     * 组件公式参数
     */
    private String formulaField;

    /**
     * 组件详情
     */
    private String componentDetail;

    /**
     * 组件公式配置
     */
    /**
     * 公式配置json
     * 后续公式配置往该json中放置
     */
    @TableField(typeHandler = JacksonTypeHandler.class)
    private ComponentFormulaConfig formulaConfig;


}
