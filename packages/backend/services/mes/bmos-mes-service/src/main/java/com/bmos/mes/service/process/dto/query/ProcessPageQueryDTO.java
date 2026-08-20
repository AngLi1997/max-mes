package com.bmos.mes.service.process.dto.query;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工艺分页查询DTO")
public class ProcessPageQueryDTO extends BasePage {

    @ApiModelProperty("工艺名称")
    private String name;

    @ApiModelProperty("产品ID")
    private Long productId;

    @ApiModelProperty("产品分类id")
    private Long productCategoryId;

    @ApiModelProperty(hidden = true)
    private List<Long> categoryIds;

    @ApiModelProperty(hidden = true)
    private List<Long> deptIds;
}
