package com.bmos.mes.service.station.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.constant.ProcessConstant;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.components.BusinessComponentManager;
import com.bmos.mes.service.components.model.BusinessComponentInstance;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.mapper.ProcedureStepConfigMapper;
import com.bmos.mes.service.process.mapper.ProcedureStepModelMapper;
import com.bmos.mes.service.process.model.ProcedureStepConfig;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.station.service.IStationService;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:38
 */
@Service
public class StationServiceImpl implements IStationService {

    @Resource
    private ProcedureStepModelMapper procedureStepModelMapper;

    @Resource
    private ProcedureStepConfigMapper procedureStepConfigMapper;

    @Resource
    private FactoryFeign factoryFeign;

    @Resource
    private PlanMapper planMapper;

    @Resource
    private BusinessComponentManager businessComponentManager;

    @Override
    public List<Long> getStationIdsByProcedureStepModelIdAndComponentId(Long procedureStepModelId, Long componentId, Long planId) {
        ProcedureStepModel step = procedureStepModelMapper.selectById(procedureStepModelId);
        if (step == null){
            return new ArrayList<>();
        }
        // 查询工位 优先查询组件中配置的
        List<String> stationPathIds = Optional.of(step)
                .map(procedureStepModel -> procedureStepConfigMapper.selectComponentConfig(
                        procedureStepModel.getId(),
                        componentId,
                        procedureStepModel.getReusable(),
                        procedureStepModel.getProcessId(),
                        procedureStepModel.getProcessVersion()))
                .map(ProcedureStepConfig::getConfigInfo)
                .map(configStr -> JSONUtil.parseObj(configStr).getJSONArray(ProcessConstant.stationPathField))
                .map(s -> s.toList(String.class))
                .orElse(new ArrayList<>());
        Plan plan = planMapper.selectById(planId);
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        // 组件未配置的 查询生产计划产线下的所有工位
        if (CollectionUtil.isEmpty(stationPathIds)) {
            return Optional.of(plan)
                    .map(productionLineId -> FeignUtils.handleRequest(plId -> factoryFeign.getStationInfoByLineId(plId),
                            plan.getProductionLineId()).getData())
                    .map(factoryStationFeignVOS -> CollectionUtils.convertList(factoryStationFeignVOS,
                            FactoryStationFeignVO::getId))
                    .orElse(new ArrayList<>());
        }
        List<String> filter =
                stationPathIds.stream().filter(e -> e.startsWith(String.valueOf(plan.getProductionLineId()))).collect(Collectors.toList());
        if (CollUtil.isEmpty(filter)) {
            return new ArrayList<>();
        }
        // 过滤出配置的属于当前生产计划产线的工位
        return filter.stream().map(e -> {
            List<String> split = StrUtil.split(e, StrUtil.DASHED);
            return Long.valueOf(CollUtil.getLast(split));
        }).collect(Collectors.toList());
    }

    @Override
    public List<Long> getStationIdListByComponentInstanceId(Long componentInstanceId) {
        BusinessComponentInstance componentInstance = businessComponentManager.getComponentInstanceById(componentInstanceId);
        if (componentInstance == null){
            return new ArrayList<>();
        }
        return getStationIdsByProcedureStepModelIdAndComponentId(componentInstance.getProcedureStepModelId(), componentInstance.getComponentId(), componentInstance.getProductPlanId());
    }
}
