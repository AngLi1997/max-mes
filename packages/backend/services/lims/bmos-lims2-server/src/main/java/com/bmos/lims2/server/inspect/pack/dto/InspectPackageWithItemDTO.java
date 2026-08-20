package com.bmos.lims2.server.inspect.pack.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 实验包(BmExperimentPackage)实体类
 *
 * @author makejava
 * @since 2024-03-02 12:46:27
 */
@Getter
@Setter
public class InspectPackageWithItemDTO extends InspectPackageDTO {
    /**
     * 实验包下检验项目
     */
    @ApiModelProperty(value = "实验包下检验项目")
    private List<InspectPackageItemDTO> packageItemList;
}

