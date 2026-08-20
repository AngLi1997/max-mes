package com.bmos.mes.service.audit.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 流程工艺绑定关系dto
 */
@Getter
@Setter
@ApiModel(value = "流程工艺绑定关系dto")
public class FlowProcessBindDTO {

    /**
     * 流程id
     */
    @ApiModelProperty(value = "流程id")
    @NotNull
    private Long id;

    /**
     * 工艺id集合
     */
    @ApiModelProperty(value = "工艺id集合")
    @NotNull
    private List<Long> processIds;

}
