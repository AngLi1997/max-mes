package com.bmos.mes.service.product.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ApiModel("批记录绑定DTO")
public class RecordSaveDTO {

    @ApiModelProperty("产品id")
    @NotNull
    private Long productId;

    @ApiModelProperty("批记录id列表")
    private List<Long> recordIds;

}
