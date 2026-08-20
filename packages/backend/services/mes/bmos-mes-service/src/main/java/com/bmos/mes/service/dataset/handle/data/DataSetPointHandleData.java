package com.bmos.mes.service.dataset.handle.data;

import com.bmos.mes.service.dataset.enums.DatasetType;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DataSetPointHandleData {

    /**
     * 当前批次id生成的批签发对应的模板版本的数据集 格式:数据集索引
     */
    private String dataSet;

    /**
     * 当前批次id生成的批签发对应的模板版本对应的数据点 数据点索引
     */
    private String dataPoint;

    /**
     * 数据集类型
     */
    private DatasetType datasetType;

}
