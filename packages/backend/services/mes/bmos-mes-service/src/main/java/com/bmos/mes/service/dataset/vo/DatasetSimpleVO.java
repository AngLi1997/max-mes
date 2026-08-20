package com.bmos.mes.service.dataset.vo;

import com.bmos.mes.service.dataset.enums.DatasetType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据集简单vo
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 11:13
 */
@Data
@ApiModel("数据集简单vo")
public class DatasetSimpleVO {

    @ApiModelProperty(value = "数据集id", example = "1")
    private Long id;

    @ApiModelProperty(value = "数据集名称", example = "数据集名称")
    private String name;

    @ApiModelEnumProperty(enumClass = DatasetType.class, value = "数据集类型")
    private DatasetType type;

    @ApiModelProperty(value = "数据集key(流水号)", example = "1")
    private String datasetKey;
}
