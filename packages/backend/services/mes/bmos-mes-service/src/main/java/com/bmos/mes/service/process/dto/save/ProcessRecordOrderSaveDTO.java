package com.bmos.mes.service.process.dto.save;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Tolerate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工艺记录项顺序保存DTO")
@Builder
public class ProcessRecordOrderSaveDTO {
    @Tolerate
    public ProcessRecordOrderSaveDTO(){}

    @ApiModelProperty("工艺id")
    @NotNull
    private Long processId;

    @ApiModelProperty("工艺版本id")
    @NotNull
    private Long processVersionId;

    @ApiModelProperty("工艺版本")
    @NotNull
    private String processVersion;

    @ApiModelProperty("记录项顺序")
    @NotEmpty
    private List<ProcessRecordOrderSaveItemDTO> recordOrders;
}
