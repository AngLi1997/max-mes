package com.bmos.mes.service.plan.document.service;

import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.execute.model.ExecuteAttachment;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.model.ProcessRecordOrder;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import com.bmos.mes.service.record.model.BatchRecordItem;
import com.bmos.mes.service.record.vo.ProcessRecordItemVO;
import com.bmos.mes.service.record.vo.RecordItemListVO;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author yigaohui
 * @date 2024/6/7
 **/
@Data
@Accessors(chain = true)
public class ProcessStepArchiveContext {
    private Plan plan;
    private List<ProcessRecordOrder> orders;
    private List<ExecuteAttachment> attachments;

    private List<BatchRecordItem> recordItemVOS;

    Map<Long, Map<Long, Long>> orderMap;

    private Map<Long, Map<Long, Map<Long, List<ExecuteAttachment>>>> attachmentMap;

    private Map<Long, BatchRecordItem> itemsMap;

    private Map<Long, List<BatchRecordComponent>> itemComponentsMap;

    private Map<Long,ProcedureModel> procedureModelMap;

    public ProcessStepArchiveContext(Plan plan, List<ProcedureModel> procedureModels, List<BatchRecordItem> recordItemVOS,
                                     List<ProcessRecordOrder> orders,
                                     List<ExecuteAttachment> attachments) {
        this.plan = plan;
        this.orders = orders;
        this.attachments = attachments;
        this.procedureModelMap = procedureModels.stream().collect(Collectors.toMap(ProcedureModel::getId, Function.identity()));
        this.orderMap = orders.stream()
                .collect(Collectors.groupingBy(ProcessRecordOrder::getRecordItemId,
                        Collectors.groupingBy(ProcessRecordOrder::getProcedureStepModelId,
                                Collectors.collectingAndThen(
                                        Collectors.collectingAndThen(
                                                Collectors.maxBy(Comparator.comparing(ProcessRecordOrder::getRecordItemOrder)),
                                                Optional::get),
                                        ProcessRecordOrder::getRecordItemOrder)
                        )
                        )
                );
        this.attachmentMap =
                attachments.stream().collect(Collectors.groupingBy(ExecuteAttachment::getRecordItemId,
                        Collectors.groupingBy(ExecuteAttachment::getProcedureStepId,
                                Collectors.groupingBy(ExecuteAttachment::getCopyVersion))));
        this.recordItemVOS = recordItemVOS;
        this.itemsMap = CollectionUtils.convertMap(this.recordItemVOS, BatchRecordItem::getItemId);
        this.itemComponentsMap = new HashMap<>();
    }

    public void putItemComponent(Long recordItemId, List<BatchRecordComponent> itemComponentList) {
        itemComponentsMap.put(recordItemId, itemComponentList);
    }
}
