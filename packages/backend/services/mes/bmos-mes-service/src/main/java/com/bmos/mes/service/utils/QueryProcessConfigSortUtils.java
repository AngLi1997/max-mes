package com.bmos.mes.service.utils;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureModelService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 获取工艺配置工序排序号/工步排序号
 * @author renjinguang
 */
public class QueryProcessConfigSortUtils {

    private static ProcedureModelService procedureModelService;

    private static ProcedureStepModelService stepModelService;

    public static void init(ProcedureModelService procedureModelService,ProcedureStepModelService stepModelService){
        QueryProcessConfigSortUtils.procedureModelService = procedureModelService;
        QueryProcessConfigSortUtils.stepModelService = stepModelService;
    }

    /**
     * 根据工序id获取工序排序号
     * @param modelIdList
     * @return key:工序id，value:工序排序号
     */
    public static Map<Long,Integer> queryProcedureModelSortByIdList(List<Long> modelIdList){
        if (CollUtil.isEmpty(modelIdList)){
            return new HashMap<>();
        }
        List<ProcedureModel> modelList = procedureModelService.getByIds(modelIdList);
        boolean hasNoSort = modelList.stream().anyMatch(e -> e.getSort() == null);
        // 存在未排序数据则认为排序异常 按id排序
        if (hasNoSort){
            AtomicInteger atomicInteger = new AtomicInteger(0);
            modelList.sort(Comparator.comparing(ProcedureModel::getId));
            modelList.forEach(e->e.setSort(atomicInteger.getAndIncrement()));
        }
        return CollectionUtils.convertMap(modelList, ProcedureModel::getId, ProcedureModel::getSort);
    }

    /**
     * 根据工步id获取工步排序号
     * @param stepModelIdList
     * @return map key:工步id,value:工步排序号
     */
    public static Map<Long, Integer> queryProcedureStepModelSortByIdList(Collection<Long> stepModelIdList){
        if (CollUtil.isEmpty(stepModelIdList)){
            return new HashMap<>();
        }
        List<ProcedureStepModel> stepModelList = stepModelService.getByIdList(new ArrayList<>(stepModelIdList));
        boolean hasNoSort = stepModelList.stream().anyMatch(e -> e.getSort() == null);
        if (hasNoSort){
            AtomicInteger atomicInteger = new AtomicInteger(0);
            stepModelList.sort(Comparator.comparing(ProcedureStepModel::getId));
            stepModelList.forEach(e->e.setSort(atomicInteger.getAndIncrement()));
        }
        return CollectionUtils.convertMap(stepModelList, ProcedureStepModel::getId, ProcedureStepModel::getSort);
    }

}
