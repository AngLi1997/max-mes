package com.bmos.mes.service.output.weigh.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * 产出物料列表项
 * @author liang
 * @version 1.0.0
 * @date 2024/5/11 10:31
 */
@Data
@ApiModel("产出物料列表项")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OutputMaterialItem {

    /**
     * 物料id
     */
    @ApiModelProperty(value = "物料id", example = "1")
    private Long id;

    /**
     * 物料名称
     */
    @ApiModelProperty(value = "物料名称", example = "矿泉水")
    private String name;

    /**
     * 物料规格
     */
    @ApiModelProperty(value = "物料规格", example = "500ml")
    private String specification;

    /**
     * 合并编码
     */
    @ApiModelProperty(value = "合并编码", example = "0001-0001")
    private String mergeCode;

    /**
     * 可选批次列表
     */
    @ApiModelProperty(value = "可选批次列表")
    private Set<String> batchNoList = new HashSet<>();
}
