package com.bmos.mes.service.trace.material.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mybatis.dataobject.BaseDO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Objects;

/**
 * 物料追溯模板信息
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 10:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "bm_material_trace_template", autoResultMap = true)
public class MaterialTraceTemplateDO extends BaseDO {

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 产品id
     */
    private Long productId;

    /**
     * 工艺id
     */
    private Long processId;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 物料树
     */
    @TableField(value = "material_tree", typeHandler = JacksonTypeHandler.class)
    private List<MaterialTraceTemplateMaterial> materialTree;

    public List<MaterialTraceTemplateMaterial> getMaterialTree() {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(materialTree, new TypeReference<List<MaterialTraceTemplateMaterial>>(){});
    }

    /**
     * 是否启用
     * @return true 启用 false 停用
     */
    public boolean getEnabled() {
        return Objects.equals(enabled, true);
    }
}
