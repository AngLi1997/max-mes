package com.bmos.mes.service.dataset.handle;

import com.bmos.mes.service.dataset.common.DatasetTrans;
import com.bmos.mes.service.dataset.handle.data.AssembleCompleteData;
import com.bmos.mes.service.dataset.handle.data.AssemblePrepareData;
import com.bmos.mes.service.dataset.handle.data.PlanBatchDocumentData;
import com.bmos.mes.service.plan.info.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AssembleDataBuilder {

    @Autowired
    private AssemblePrepareBuilder assemblePrepareBuilder;

    @Autowired
    PlanService planService;

    public AssembleCompleteData build(PlanBatchDocumentData documentData) {
        // 需要组装该对象需要的所有的前置数据
        AssemblePrepareData assemblePrepareData = assemblePrepareBuilder.build(documentData);
        // 开始进行数据组装
        List<DatasetTrans> datasetTransList = DatasetTransBuilder.build(assemblePrepareData);
        return new AssembleCompleteData(assemblePrepareData.getRenderTemplateDataList(), datasetTransList, assemblePrepareData.getDataSetProcess());
    }

}
