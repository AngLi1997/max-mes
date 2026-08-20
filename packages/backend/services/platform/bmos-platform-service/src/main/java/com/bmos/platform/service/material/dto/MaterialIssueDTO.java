package com.bmos.platform.service.material.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ApiModel("物料下发DTO")
public class MaterialIssueDTO {

    @ApiModelProperty("物料ids")
    List<Long> materialIds;

    @ApiModelProperty("物料分类ids")
    @NotEmpty
    List<Long> materialCategoryIds;

    @ApiModelProperty("物料下发业务")
    @NotEmpty
    List<MaterialIssueBusinessDTO> businesses;
}
