package com.bmos.mes.service.dataset.dto;

import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.mybatis.page.BasePage;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据集分页查询query
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:20
 */
@Data
@ApiModel("数据集分页查询query")
@EqualsAndHashCode(callSuper = true)
public class DatasetPageQuery extends BasePage {

    @ApiModelProperty(value = "数据集分类id", example = "1")
    private Long datasetCategoryId;

    @ApiModelEnumProperty(value = "数据集类型", enumClass = DatasetType.class)
    private String datasetType;

    @ApiModelProperty(value = "数据集名称", example = "1")
    private String name;
}
