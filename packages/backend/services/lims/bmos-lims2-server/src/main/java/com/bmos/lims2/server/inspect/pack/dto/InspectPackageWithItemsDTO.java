package com.bmos.lims2.server.inspect.pack.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 实验包分页DTO - 包含关联的检验项目信息
 * @author system
 */
@Getter
@Setter
@ApiModel("实验包分页DTO - 包含检验项目信息")
public class InspectPackageWithItemsDTO extends InspectPackageDTO {

    /**
     * 关联的检验项目列表
     */
    @ApiModelProperty(value = "关联的检验项目列表")
    private List<InspectPackageItemDTO> packageItemList;
}