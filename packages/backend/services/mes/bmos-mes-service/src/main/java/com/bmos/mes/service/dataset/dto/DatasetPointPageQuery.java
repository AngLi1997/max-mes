package com.bmos.mes.service.dataset.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据点分页查询query
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:20
 */
@Data
@ApiModel("数据点分页查询query")
@EqualsAndHashCode(callSuper = true)
public class DatasetPointPageQuery extends BasePage {

    @ApiModelProperty(value = "数据集id", example = "1")
    private Long datasetId;

    @ApiModelProperty(value = "数据点名称", example = "1")
    private String name;
}
