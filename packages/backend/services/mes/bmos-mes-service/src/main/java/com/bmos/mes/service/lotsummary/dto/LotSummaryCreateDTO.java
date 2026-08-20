package com.bmos.mes.service.lotsummary.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 批次摘要创建DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/9/5 10:36
 */
@Data
@ApiModel("批次摘要创建DTO")
public class LotSummaryCreateDTO {

    @ApiModelProperty(value = "产品id", example = "1")
    @NotNull
    private Long productId;

    @ApiModelProperty(value = "批次摘要名称", example = "批次摘要名称")
    @NotBlank
    @Length(max = 100)
    private String name;

    @ApiModelProperty(value = "工艺id", example = "1")
    @NotNull
    private Long processId;

    @ApiModelProperty(value = "数据集点数据")
    @Valid
    @NotEmpty
    private List<LotSummaryItemDTO> list;
}
