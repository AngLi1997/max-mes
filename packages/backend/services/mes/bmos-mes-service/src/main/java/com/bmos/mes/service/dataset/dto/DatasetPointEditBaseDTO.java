package com.bmos.mes.service.dataset.dto;

import com.bmos.mes.service.dataset.model.DatasetPoint;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据点编辑基类
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 17:10
 */
@Data
@ApiModel("数据点编辑基类")
public abstract class DatasetPointEditBaseDTO {

    @ApiModelProperty(value = "数据点id", example = "1")
    private Long id;

    public abstract void copyToDatasetPoint(DatasetPoint datasetPoint);

    public boolean compare(DatasetPoint datasetPoint) {
        return true;
    }
}
