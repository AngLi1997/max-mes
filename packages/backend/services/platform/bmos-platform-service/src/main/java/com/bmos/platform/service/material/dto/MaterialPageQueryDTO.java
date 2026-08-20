package com.bmos.platform.service.material.dto;

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
@ApiModel("物料分页查询DTO")
public class MaterialPageQueryDTO extends BasePage {

    @ApiModelProperty("物料分类id")
    private Long materialCategoryId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    private List<Long> materialCategoryIds;
}
