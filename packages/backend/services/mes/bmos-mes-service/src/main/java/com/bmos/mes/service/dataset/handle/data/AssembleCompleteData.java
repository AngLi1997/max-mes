package com.bmos.mes.service.dataset.handle.data;

import com.bmos.mes.service.dataset.common.DatasetTrans;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AssembleCompleteData {

    /**
     * 需要渲染的模板
     */
    private List<RenderTemplateData> renderTemplateUrl;

    /**
     * 模板渲染所需要的数据
     */
    private List<DatasetTrans> datasetTransList;

    /**
     * 数据集对应的工艺 key为数据集索引 value为工艺id
     */
    private Map<String, Long> dataSetProcessIdMap;
}
