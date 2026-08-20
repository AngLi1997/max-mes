package com.bmos.lims2.server.inspect.document.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@ApiModel("请验单配置更新DTO")
public class DocumentConfigUpdateDTO extends DocumentConfigSaveDTO{

    @ApiModelProperty(value = "请验单id", required = true)
    @NotNull
    private Long id;

}
