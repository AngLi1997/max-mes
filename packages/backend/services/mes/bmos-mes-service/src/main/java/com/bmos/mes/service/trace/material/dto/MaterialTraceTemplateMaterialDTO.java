package com.bmos.mes.service.trace.material.dto;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mes.service.trace.material.entity.PercentYieldRange;
import com.bmos.mes.service.trace.material.enums.MaterialTraceType;
import com.bmos.mybatis.CustomIdGenerator;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.collections.CollectionUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 物料模板关联物料信息
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 17:24
 */
@Data
@ApiModel("物料模板关联物料信息")
public class MaterialTraceTemplateMaterialDTO {

    @ApiModelProperty(value = "物料关联id", example = "1")
    private Long id = CustomIdGenerator.nextId();

    @ApiModelProperty(value = "物料id", example = "1")
    private Long materialId;

    @ApiModelEnumProperty(value = "物料类型", enumClass = CategoryInfoTypeEnum.class)
    private Integer materialType;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelProperty(value = "是否显示收率", example = "true")
    private Boolean showPercentYield = true;

    @ApiModelProperty(value = "收率范围")
    @Valid
    private PercentYieldRange percentYieldRange;

    @ApiModelProperty(value = "是否参与物料平衡计算", example = "true")
    private Boolean calcFlag = true;

    @ApiModelProperty(value = "子物料列表")
    @Valid
    private List<MaterialTraceTemplateMaterialDTO> children;

    @ApiModelProperty(value = "关联工序步骤列表")
    @Valid
    private List<ProcedureStepDTO> procedureStepDTOList = new ArrayList<>();

    public List<ProcedureStepDTO> getAllProcedureStepDTOList() {
        List<ProcedureStepDTO> result = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(procedureStepDTOList)){
            procedureStepDTOList.forEach(procedureStepDTO -> {
                procedureStepDTO.setRelationId(this.id);
                procedureStepDTO.setMaterialId(this.materialId);
            });
            result.addAll(procedureStepDTOList);
        }
        if (CollectionUtils.isNotEmpty(children)){
            for (MaterialTraceTemplateMaterialDTO child : children) {
                result.addAll(child.getAllProcedureStepDTOList());
            }
        }
        return result;
    }

    @Data
    @ApiModel("物料物料关联工步信息")
    public static class ProcedureStepDTO {

        @ApiModelProperty(value = "工步关联id", example = "1")
        private Long id;

        @ApiModelProperty(value = "工序步骤id", example = "1")
        @NotNull
        private Long procedureStepId;

        @ApiModelEnumProperty(value = "物料追溯类型", enumClass = MaterialTraceType.class)
        @NotNull
        private Integer traceType;

        @JsonIgnore
        private Long relationId;

        @JsonIgnore
        private Long materialId;
    }
}
