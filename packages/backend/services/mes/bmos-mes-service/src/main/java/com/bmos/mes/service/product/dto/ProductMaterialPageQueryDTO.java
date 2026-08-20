package com.bmos.mes.service.product.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import com.bmos.mybatis.page.BasePage;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("物料分页查询DTO")
public class ProductMaterialPageQueryDTO extends BasePage {

    @ApiModelProperty("物料分类id")
    private Long materialCategoryId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("合并编码")
    private String mergeCode;

    @ApiModelEnumProperty(value = "类别信息类型",required = true,enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    @NotNull
    private Integer categoryType;

    @ApiModelProperty("启停状态")
    private Boolean status;
    
    private List<Long> materialCategoryIds;
}
