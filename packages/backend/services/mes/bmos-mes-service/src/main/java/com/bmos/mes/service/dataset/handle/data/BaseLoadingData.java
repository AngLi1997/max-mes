package com.bmos.mes.service.dataset.handle.data;

import com.bmos.mes.service.dataset.common.enums.DatasetTransValueDataType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BaseLoadingData {

    /**
     * 表单值
     */
    private String value;


    /**
     * 复制版本
     */
    private Long copyVersion;

    /**
     * 工序换班次数
     */
    private Integer procedureChangeNumber;

    /**
     * 工艺换班次数
     */
    private Integer processChangeNumber;

    /**
     * 工艺名称
     */
    private String processName;

    /**
     * 工序名称
     */
    private String procedureName;

    /**
     * 值类型
     */
    private DatasetTransValueDataType type;

    /**
     * 是否复用
     */
    private Boolean reuse;

    /**
     * 当前批次生成的批签发对应的数据集以及数据点
     */
    private List<DataSetPointHandleData> dataSetPointHandleDataList;

}
