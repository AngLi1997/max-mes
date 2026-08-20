package com.bmos.mes.service.process.dto.save;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工艺关联保存DTO")
public class ProcessRelationDTO {

    @ApiModelProperty("关联工艺id")
    @NotNull
    private Long relationProcessId;

    @ApiModelProperty("关联物料id集合")
    private List<Long> materialIds;
}
