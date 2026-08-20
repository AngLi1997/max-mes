package com.bmos.mes.service.dataset.dto;

import com.bmos.mes.service.dataset.model.DatasetPoint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 批签发引用编辑DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:48
 */
@Data
@ApiModel("批签发引用编辑DTO")
@EqualsAndHashCode(callSuper = true)
public class DatasetLotReleaseLinkEditDTO extends DatasetPointEditBaseDTO {

    @ApiModelProperty(value = "批签发模版id", example = "1")
    @NotNull
    private Long lotReleaseTemplateId;

    @ApiModelProperty(value = "批签发版本", example = "V1")
    @NotBlank
    private String lotReleaseVersion;

    @ApiModelProperty(value = "批签发模版名称", example = "1")
    @NotBlank
    private String lotReleaseTemplateName;

    @ApiModelProperty(value = "批签发引用参数范围", example = "P15:S19")
    @NotBlank
    private String linkArea;

    @Override
    public void copyToDatasetPoint(DatasetPoint datasetPoint) {
        datasetPoint.setLotReleaseTemplateId(this.lotReleaseTemplateId);
        datasetPoint.setLotReleaseVersion(this.lotReleaseVersion);
        datasetPoint.setLinkArea(this.linkArea);
        datasetPoint.setName(lotReleaseTemplateName);
    }
}
