package com.bmos.mes.service.exception.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@ApiModel("异常分页查询DTO")
@Data
public class ExceptionPageQueryDTO extends BasePage {

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("异常类型")
    private String exceptionType;

    @ApiModelProperty("异常描述")
    private String exceptionDescription;

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("调查中")
    @NotNull
    private Boolean investigating;

}
