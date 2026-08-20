package com.bmos.wms.service.platform.material.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.wms.common.enums.inventory.CategoryInfoTypeEnum;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ApiModel("同步分类物料树查询DTO")
public class SyncTreeQueryDTO {

    @NotNull
    private Long parentId;

    private String keyword;

    @ApiModelEnumProperty(value = "类别信息类型", required = true, enumClass = CategoryInfoTypeEnum.class)
    @EnumValidate(value = CategoryInfoTypeEnum.class)
    @NotNull
    private Integer categoryType;

}
