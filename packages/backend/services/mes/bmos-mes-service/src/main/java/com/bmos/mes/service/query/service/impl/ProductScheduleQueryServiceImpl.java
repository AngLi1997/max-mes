package com.bmos.mes.service.query.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.plan.info.service.PlanService;
import com.bmos.mes.service.process.mapper.ProcedureModelMapper;
import com.bmos.mes.service.process.mapper.ProductScheduleProcedureConfigMapper;
import com.bmos.mes.service.process.model.ProcedureModel;
import com.bmos.mes.service.process.model.ProductScheduleProcedureConfig;
import com.bmos.mes.service.query.dto.ProcedureInProductionDTO;
import com.bmos.mes.service.query.dto.ProductScheduleProcedureConfigDTO;
import com.bmos.mes.service.query.service.IProductScheduleQueryService;
import com.bmos.orchestrator.engine.core.query.resp.ExecutionBusinessKeyResp;
import com.bmos.orchestrator.engine.core.query.service.ExecutionQueryService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 生产进度查询服务实现
 *
 * @className: ProductScheduleQueryServiceImpl
 * @author: yigaohui
 * @date: 2024/12/4 18:47
 * @Version: 1.0
 * @description:
 */
@Service
public class ProductScheduleQueryServiceImpl implements IProductScheduleQueryService {


    @Autowired
    private ProductScheduleProcedureConfigMapper productScheduleProcedureConfigMapper;
    @Autowired
    private ProcedureModelMapper procedureModelMapper;

    @Autowired
    private ExecutionQueryService executionQueryService;

    @Autowired
    private PlanService planService;

    /**
     * 1. 根据配置的工序，找到工序中存储的流程节点elementKey
     * 2. 拿到elementKey查询流程节点表，找到正在进行的工序
     * 3. 返回正在进行的工序，工序节点带上了流程实例id
     * 4. 通过流程实例id到流程实例表中查询流程实例中的计划id
     * 5. 通过计划id查询到计划
     */
    @Override
    public List<ProcedureInProductionDTO> inProduction() {
        List<ProductScheduleProcedureConfigDTO> productScheduleProcedureConfigs = productScheduleProcedureConfigMapper.selectListWithProcesName();
        if (CollectionUtil.isEmpty(productScheduleProcedureConfigs)) {
            return Lists.newArrayList();
        }
        LambdaQueryWrapper<ProcedureModel> modelQuery = new QueryWrapper<ProcedureModel>().lambda();
        modelQuery.in(ProcedureModel::getProcedureId, productScheduleProcedureConfigs.stream().map(ProductScheduleProcedureConfigDTO::getProcedureId).collect(Collectors.toList()));
        List<ProcedureModel> procedureModels = procedureModelMapper.selectList(modelQuery);
        if (CollectionUtil.isEmpty(procedureModels)) {
            return Lists.newArrayList();
        }
        List<String> nodeIds = procedureModels.stream().map(ProcedureModel::getNodeId).collect(Collectors.toList());
        List<ExecutionBusinessKeyResp> inactionNodeBusiness = executionQueryService.findActiveBusinessListByNodeIdS(nodeIds);
        if (CollectionUtil.isEmpty(inactionNodeBusiness)) {
            return Lists.newArrayList();
        }
        // 通过流程查找节点正在进行的计划id
        List<Long> planIds = inactionNodeBusiness.stream().map(ExecutionBusinessKeyResp::getBusinessKeyList).flatMap(Collection::stream).map(Long::valueOf).collect(Collectors.toList());
        List<Plan> plans = planService.listByIds(planIds);
        if (CollectionUtil.isEmpty(plans)) {
            return Lists.newArrayList();
        }
        Map<String, List<ExecutionBusinessKeyResp>> nodeMap = inactionNodeBusiness.stream().collect(Collectors.groupingBy(ExecutionBusinessKeyResp::getNodeId));
        Map<Long, List<ProcedureModel>> procedureModelMap = procedureModels.stream().collect(Collectors.groupingBy(ProcedureModel::getProcedureId));
        Map<Long, Plan> planMap = plans.stream().collect(Collectors.toMap(Plan::getId, Function.identity()));
        List<ProcedureInProductionDTO> res = new ArrayList<>();
        productScheduleProcedureConfigs.forEach(item -> {
            ProcedureInProductionDTO procedureInProductionDTO = BeanUtil.copyProperties(item, ProcedureInProductionDTO.class);
            procedureInProductionDTO.setInProductionBatchNoList(new ArrayList<>());
            res.add(procedureInProductionDTO);
            List<ProcedureModel> modelList = procedureModelMap.get(item.getProcedureId());
            if (CollectionUtil.isNotEmpty(modelList)) {
                List<String> nodes = modelList.stream().map(ProcedureModel::getNodeId).distinct().collect(Collectors.toList());
                nodes.forEach(node -> {
                    List<ExecutionBusinessKeyResp> businessList = nodeMap.get(node);
                    if (CollectionUtil.isNotEmpty(businessList)) {
                        businessList.forEach(business -> {
                            if (!CollectionUtil.isEmpty(business.getBusinessKeyList())) {
                                business.getBusinessKeyList().forEach(businessKey -> {
                                    Plan plan = planMap.get(Long.valueOf(businessKey));
                                    if (plan != null) {
                                        procedureInProductionDTO.getInProductionBatchNoList().add(plan.getBatchNo());
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        return res;
    }
}
