package com.bmos.mes.service.process.dto.save;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("保存关联工艺DTO")
public class ProcessRelationSaveDTO {


    @ApiModelProperty(value = "工艺id",required = true)
    @NotNull
    private Long processId;

    @ApiModelProperty("关联信息")
    private List<ProcessRelationDTO> relations;
}
