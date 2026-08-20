package com.bmos.mes.service.lotrelease.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批签发生成DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/20 13:33
 */
@Data
@ApiModel(value = "批签发生成DTO")
public class LotReleaseGenerateDTO {

    @ApiModelProperty(value = "批签发模板id", example = "1")
    private Long lotReleaseTemplateId;

    @ApiModelProperty(value = "批签发版本", example = "1")
    private String lotReleaseVersion;

    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    @ApiModelProperty(value = "生产计划id", example = "1")
    private Long planId;

    @ApiModelProperty(value = "动态数据", example = "1")
    @Valid
    private List<DynamicData> dynamicData;

    @ApiModelProperty(value = "批次引用数据", example = "1")
    @Valid
    private List<BatchLink> batchLinksData;

    @ApiModelProperty(value = "是否为验证生生成", example = "1")
    @NotNull
    private Boolean isValid = false;


    @ApiModel("批签发生成 - 动态数据填报")
    @Data
    public static class DynamicData {

        @ApiModelProperty(value = "动态数据点key", example = "1")
        @NotBlank
        private String datasetPointKey;

        @ApiModelProperty(value = "数据集key", example = "1")
        @NotBlank
        private String datasetKey;

        @ApiModelProperty(value = "动态数据值", example = "1")
        private String value;
    }

    @ApiModel("批签发生成 - 批次引用数据(带顺序)")
    @Data
    public static class BatchLink {

        @ApiModelProperty(value = "生产批次id", example = "1")
        private Long planId;
    }
}
