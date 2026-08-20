package com.bmos.mes.service.formula.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("产品配方分页查询DTO")
@Data
public class ProductFormulaPageQueryDTO extends BasePage {

    @ApiModelProperty("配方名称")
    private String name;

    @ApiModelProperty("产品id")
    private Long productId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty(hidden = true)
    private List<Long> productIdList;

    @ApiModelProperty(hidden = true)
    private List<Long> deptIds;

}
