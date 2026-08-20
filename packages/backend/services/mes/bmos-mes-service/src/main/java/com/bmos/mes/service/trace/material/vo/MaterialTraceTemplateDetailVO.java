package com.bmos.mes.service.trace.material.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.bmos.mes.service.trace.material.entity.MaterialTraceTemplateMaterial;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 物料追溯模板详情
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 15:12
 */
@Data
public class MaterialTraceTemplateDetailVO {

    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    @ApiModelProperty(value = "模板名称", example = "模板名称")
    private String templateName;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "产品名称", example = "产品名称")
    private String productName;

    @ApiModelProperty(value = "产品编码", example = "产品编码")
    private String mergeCode;

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    @ApiModelProperty(value = "工艺名称", example = "工艺名称")
    private String processName;

    @ApiModelProperty(value = "是否启用", example = "true")
    private Boolean enabled;

    @ApiModelProperty(value = "物料树")
    @TableField(value = "material_tree", typeHandler = JacksonTypeHandler.class)
    private List<MaterialTraceTemplateMaterial> materialTree;

    @ApiModelProperty(value = "物料追溯物料关联工步信息映射列表")
    private List<ProcedureStepData> procedureStepDataList;


    @Data
    @ApiModel("物料追溯物料关联工步信息映射")
    public static class ProcedureStepData {

        @ApiModelProperty(value = "物料关联id", example = "1")
        private Long relationId;

        @ApiModelProperty(value = "关联工序步骤列表(产出)")
        private List<ProcedureStepVO> outputStepList = new ArrayList<>();

        @ApiModelProperty(value = "关联工序步骤列表(消耗)")
        private List<ProcedureStepVO> consumeStepList = new ArrayList<>();
    }

    @Data
    @ApiModel("物料追溯物料关联工步信息VO")
    public static class ProcedureStepVO {

        @ApiModelProperty(value = "id", example = "1")
        private Long id;

        @ApiModelProperty(value = "物料id", example = "1")
        private Long materialId;

        @ApiModelProperty(value = "工艺id", example = "1")
        private Long processId;

        @ApiModelProperty(value = "工艺名称", example = "工艺名称")
        private String processName;

        @ApiModelProperty(value = "工艺版本", example = "1")
        private String processVersion;

        @ApiModelProperty(value = "工序id", example = "1")
        private Long procedureId;

        @ApiModelProperty(value = "工序名称", example = "工序名称")
        private String procedureName;

        @ApiModelProperty(value = "工序步骤id", example = "1")
        private Long procedureStepId;

        @ApiModelProperty(value = "工序步骤名称", example = "工序步骤名称")
        private String procedureStepName;
    }
}
