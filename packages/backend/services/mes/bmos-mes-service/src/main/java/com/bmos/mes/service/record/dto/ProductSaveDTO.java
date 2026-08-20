package com.bmos.mes.service.record.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel(value = "添加产品DTO")
public class ProductSaveDTO {

    @ApiModelProperty(value = "产品id")
    @NotNull
    private List<Long> productIdList;

    @ApiModelProperty(value = "记录id")
    @NotNull
    private Long recordId;

}
