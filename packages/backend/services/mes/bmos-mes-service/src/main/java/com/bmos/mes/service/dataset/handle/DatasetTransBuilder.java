package com.bmos.mes.service.dataset.handle;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.multi.RowKeyTable;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.dataset.common.DatasetTrans;
import com.bmos.mes.service.dataset.handle.data.AssemblePrepareData;
import com.bmos.mes.service.dataset.handle.data.ExecuteFormLoadingData;
import com.bmos.mes.service.dataset.util.options.ChangeNumberPhotoData;
import com.bmos.mes.service.dataset.util.options.DocxTakePhotoLegendReplaceOption;
import com.bmos.mes.service.dataset.util.options.ProcedureTakePhotoData;
import com.bmos.mes.service.dataset.util.options.ProcessTakePhotoData;
import com.bmos.mes.service.plan.info.model.Plan;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.stream.Collectors;

public class DatasetTransBuilder {

    public static List<DatasetTrans> build(AssemblePrepareData assemblePrepareData){
        Map<Long, DatasetTrans> datasetMap = new HashMap<>();
        Map<String, List<ExecuteFormLoadingData>> planMap = assemblePrepareData.getExecuteFormLoadingData().stream()
                .collect(Collectors.groupingBy(e -> StrUtil.format("{}-{}", e.getPlanId(), e.getBatchNo())));
        Map<Long, Plan> planInfoMap = assemblePrepareData.getPlanList().stream().collect(Collectors.toMap(Plan::getId, e -> e));
        // 查询所有批次下所有复制的数据
        for (Long planId : planInfoMap.keySet()) {
            Plan plan = planInfoMap.get(planId);
            String key = StrUtil.format("{}-{}", planId, plan.getBatchNo());
            DatasetTrans datasetTrans = build(plan, planMap.get(key), assemblePrepareData);
            datasetMap.put(planId, datasetTrans);
        }
        // 所有动态渲染的数据以及批签发文档链接地址的数据需要装载到当前的批次当中去
        DatasetTrans curRenderDatasetTrans = datasetMap.get(assemblePrepareData.getPlanId());
        // 动态渲染的数据
        curRenderDatasetTrans.addAllDynamicData(assemblePrepareData.getDynamicRenderingData());
        // 批签发文档链接地址的数据
        curRenderDatasetTrans.addAllPlanLoadingData(assemblePrepareData.getPlanLoadingData());
        // 需要根据传递进来的批次顺序进行生成
        return sort(datasetMap, assemblePrepareData.getSortPlanIdList());
    }

    public static DatasetTrans build(Plan plan, List<ExecuteFormLoadingData> dataList, AssemblePrepareData assemblePrepareData){
        DatasetTrans datasetTrans = new DatasetTrans();
        datasetTrans.setPlanId(plan.getId());
        datasetTrans.setProcessId(plan.getProcessId());
        datasetTrans.setBatchNo(plan.getBatchNo());
        datasetTrans.setProcessName(plan.getProcessName());
        // 数据组装
        datasetTrans.addAllFormData(dataList);
        // 排序
        datasetTrans.sortData(assemblePrepareData.getPlanCopyVersion().get(plan.getId()));
        // 将拍照附件填充到数据中
        List<DocxTakePhotoLegendReplaceOption.TakePhotoData> takePhotoData = assemblePrepareData.getPlanAttachment().get(plan.getId());
        // 对当前工艺的拍照数据进行数据组装排序
        datasetTrans.setTakePhotoDataList(buildProcessTakePhotoData(takePhotoData));
        return datasetTrans;
    }

    private static ProcessTakePhotoData buildProcessTakePhotoData(List<DocxTakePhotoLegendReplaceOption.TakePhotoData> takePhotoData) {;
        if (CollUtil.isEmpty(takePhotoData)){
            return null;
        }
        ProcessTakePhotoData processTakePhotoData = new ProcessTakePhotoData();
        processTakePhotoData.setProcessName(takePhotoData.get(0).getProcessName());
        ProcedureTakePhotoData procedureTakePhotoData = new ProcedureTakePhotoData();
        for (DocxTakePhotoLegendReplaceOption.TakePhotoData takePhotoDataItem : takePhotoData) {
            ChangeNumberPhotoData changeNumberPhotoData = procedureTakePhotoData.getOrDefault(takePhotoDataItem.getProcedureName(), new ChangeNumberPhotoData());
            RowKeyTable<Integer, Integer, List<DocxTakePhotoLegendReplaceOption.TakePhotoData>> photoTable = changeNumberPhotoData.getPhotoTable();
            if (Objects.isNull(photoTable)){
                photoTable = new RowKeyTable<>();
                changeNumberPhotoData.setPhotoTable(photoTable);
            }
            List<DocxTakePhotoLegendReplaceOption.TakePhotoData> curRenderList = photoTable.get(takePhotoDataItem.getProcessChangeNumber(), takePhotoDataItem.getProcedureChangeNumber());
            if (CollUtil.isNotEmpty(curRenderList)){
                curRenderList.add(takePhotoDataItem);
            } else {
                photoTable.put(takePhotoDataItem.getProcessChangeNumber(), takePhotoDataItem.getProcedureChangeNumber(), Lists.newArrayList(takePhotoDataItem));

            }
            // 根据工序名称进行归类
            procedureTakePhotoData.put(takePhotoDataItem.getProcedureName(), changeNumberPhotoData);
        }
        processTakePhotoData.setProcedureTakePhotoData(procedureTakePhotoData);
        return processTakePhotoData;
    }

    private static List<DatasetTrans> sort(Map<Long, DatasetTrans> datasetMap, List<Long> sortPlanIdList) {
        List<DatasetTrans> datasetTransList = new ArrayList<>();
        for (Long planId : sortPlanIdList) {
            DatasetTrans datasetTrans = datasetMap.get(planId);
            datasetTransList.add(datasetTrans);
        }
        return datasetTransList;
    }

}
