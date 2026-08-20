package com.bmos.mes.service.dataset.handle.data;

import com.bmos.mes.service.dataset.common.enums.DatasetTransValueDataType;
import com.bmos.mes.service.dataset.enums.DatasetType;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态渲染的数据
 */
@Getter
@Setter
public class DynamicRenderingData extends BaseLoadingData {

    public void setSingle(String datasetKey, String datasetPointKey, String value){
        this.setValue(value);
        this.setType(DatasetTransValueDataType.TEXT);
        List<DataSetPointHandleData> list = new ArrayList<>();
        list.add(new DataSetPointHandleData(datasetKey, datasetPointKey, DatasetType.DYNAMIC_REPORT));
        this.setDataSetPointHandleDataList(list);
    }
}
