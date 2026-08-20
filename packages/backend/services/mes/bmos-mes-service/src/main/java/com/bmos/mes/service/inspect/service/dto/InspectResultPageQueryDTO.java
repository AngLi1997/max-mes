package com.bmos.mes.service.inspect.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("请验结果分页查询DTO")
@Data
public class InspectResultPageQueryDTO extends BasePage {

    @NotNull
    @ApiModelProperty("生产计划id")
    private Long productPlanId;

}
