package com.bmos.wms.service.platform.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel("向平台请求下发DTO")
@Getter
@Setter
public class MaterialIssueRequestDTO {

    @ApiModelProperty("物料ids")
    List<Long> materialIds;

    @ApiModelProperty("物料分类ids")
    @NotEmpty
    List<Long> materialCategoryIds;

    @ApiModelProperty("物料下发业务")
    @NotEmpty
    List<MaterialIssueBusinessDTO> businesses;

}
