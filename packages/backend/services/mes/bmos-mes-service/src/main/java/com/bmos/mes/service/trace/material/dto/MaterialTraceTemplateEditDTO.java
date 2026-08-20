package com.bmos.mes.service.trace.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 物料模板编辑dto
 * @author liang
 * @version 1.0.0
 * @date 2024/11/19 15:11
 */
@Data
@ApiModel("物料模板编辑dto")
public class MaterialTraceTemplateEditDTO {

    @ApiModelProperty(value = "模板id", example = "1")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "模板名称", example = "模板名称")
    @NotBlank
    @Length(max = 100)
    private String templateName;

    @ApiModelProperty(value = "产品id", example = "1")
    @NotNull
    private Long productId;

    @ApiModelProperty(value = "工艺id", example = "1")
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "物料树形结构")
    @NotEmpty
    private List<MaterialTraceTemplateMaterialDTO> materialDTOTree;

    @ApiModelProperty(value = "需要删除的工序步骤配置的id列表")
    private List<Long> stepRemoveIdList;
}
