package com.bmos.lims2.server.inspect.document.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("请验单绑定检品DTO")
public class DocumentConfigBindProductDTO {

    @ApiModelProperty("请验单id")
    @NotNull
    private Long id;

    @ApiModelProperty("检品id列表")
    private List<Long> materialIdList;

}
