package com.bmos.mes.service.formula.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@ApiModel("产品配方版本分页查询DTO")
@Getter
@Setter
public class ProductFormulaVersionPageQueryDTO extends BasePage {

    @ApiModelProperty("配方id")
    @NotNull
    private Long id;

}
