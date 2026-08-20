package com.bmos.mes.service.lotsummary.dto;

import com.bmos.mes.service.lotsummary.enums.LotSummaryItemType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 15:01
 */

@Data
@ApiModel("批次摘要数据创建DTO")
public class LotSummaryItemDTO {

    @ApiModelProperty(value = "标题名称", example = "1")
    @NotBlank
    @Length(max = 100)
    private String labelName;

    @ApiModelProperty(value = "数据点id", example = "1")
    @NotNull
    private Long datasetPointId;

    @ApiModelEnumProperty(value = "批次摘要项目类型(不传默认为数据集类型)", enumClass = LotSummaryItemType.class)
    private LotSummaryItemType lotSummaryItemType = LotSummaryItemType.DATASET_POINT;
}
