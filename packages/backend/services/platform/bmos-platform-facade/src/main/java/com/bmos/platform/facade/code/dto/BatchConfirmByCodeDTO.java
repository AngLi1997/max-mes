package com.bmos.platform.facade.code.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@ApiModel("根据信息批量确认DTO")
@Data
public class BatchConfirmByCodeDTO {

    @NotEmpty
    @Valid
    @ApiModelProperty("确认批号DTO")
    private List<ConfirmNoInfoDTO> list;

}
