package com.bmos.mes.service.execute.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.common.model.component.BasicComponentConfig;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.ExecuteEquipmentCodeQueryDTO;
import com.bmos.mes.service.execute.dto.ExecuteEquipmentQueryDTO;
import com.bmos.mes.service.execute.service.ExecuteCommonService;
import com.bmos.mes.service.execute.vo.ExecuteEquipmentVO;
import com.bmos.mes.service.plan.info.mapper.PlanMapper;
import com.bmos.mes.service.plan.info.model.Plan;
import com.bmos.mes.service.platform.FeignUtils;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.record.base.util.BasicComponentUtils;
import com.bmos.platform.facade.equipment.dto.EquipmentQueryDTO;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.factory.feign.FactoryFeign;
import com.bmos.platform.facade.factory.vo.FactoryStationFeignVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExecuteCommonServiceImpl implements ExecuteCommonService {

    @Resource
    private PlanMapper planMapper;

    @Resource
    private ProcedureStepConfigService procedureStepConfigService;

    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private FactoryFeign factoryFeign;

    @Override
    public List<ExecuteEquipmentVO> getExecuteComponentEquipmentList(ExecuteEquipmentQueryDTO dto) {
        Plan plan = planMapper.selectById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        String componentConfigJson = procedureStepConfigService.getStepComponentConfigJson(dto.getProcedureStepModelId(), dto.getComponentId());
        BasicComponentConfig componentConfig = JsonUtils.parseObject(componentConfigJson, BasicComponentConfig.class);
        if (componentConfig == null || CollUtil.isEmpty(componentConfig.getStationShow())) {
            // 若未配置工位 从生产计划下的产线获取可使用设备
            List<EquipmentInfoFeignVO> data = FeignUtils.handleRequest(equipmentConfigFeign::getConfigByProductionLineIdWithNoPermission, plan.getProductionLineId()).getData();
            return ExecuteFormDataConverter.INSTANCE.convert2ExecuteEquipmentVO(data);
        }
        List<String> stationShow = componentConfig.getStationShow();
        List<Long> stationIds = BasicComponentUtils.filterStations(stationShow, plan.getProductionLineId());
        if (CollUtil.isEmpty(stationIds)) {
            return new ArrayList<>();
        }
        EquipmentQueryDTO equipmentQueryDTO = new EquipmentQueryDTO();
        equipmentQueryDTO.setStationIdList(stationIds);
        List<EquipmentInfoFeignVO> data = FeignUtils.handleRequest(equipmentConfigFeign::getConfigByStationIdList, equipmentQueryDTO).getData();
        return ExecuteFormDataConverter.INSTANCE.convert2ExecuteEquipmentVO(data);
    }

    @Override
    public ExecuteEquipmentVO getEquipmentByCode(ExecuteEquipmentCodeQueryDTO dto) {
        ResponseInfo<EquipmentInfoFeignVO> res = equipmentConfigFeign.getEquipmentByEquipmentCodeWithoutPermission(dto.getCode());
        EquipmentInfoFeignVO data = res.getData();
        if (data == null) {
            throw new BmosException(MesResponseCode.EQUIPMENT_NOT_EXIST);
        }
        ExecuteEquipmentVO result = ExecuteFormDataConverter.INSTANCE.convert2ExecuteEquipmentVO(data);
        if (!dto.validConfig()) {
            return result;
        }
        Plan plan = planMapper.selectById(dto.getProductPlanId());
        if (plan == null) {
            throw new BmosException(MesResponseCode.PRODUCT_PLAN_NOT_EXISTS);
        }
        String componentConfigJson = procedureStepConfigService.getStepComponentConfigJson(dto.getProcedureStepModelId(), dto.getComponentId());
        BasicComponentConfig componentConfig = JsonUtils.parseObject(componentConfigJson, BasicComponentConfig.class);
        List<Long> stationIds;
        if (componentConfig == null || CollUtil.isEmpty(componentConfig.getStationShow())) {
            // 若未配置工位 查询产线下的工位
            ResponseInfo<List<FactoryStationFeignVO>> listResponseInfo = FeignUtils.handleRequest(factoryFeign::getStationInfoByLineId, plan.getProductionLineId());
            stationIds = CollectionUtils.convertList(listResponseInfo.getData(), FactoryStationFeignVO::getId);
        } else {
            List<String> stationShow = componentConfig.getStationShow();
            stationIds = BasicComponentUtils.filterStations(stationShow, plan.getProductionLineId());
        }
        stationIds.retainAll(data.getStationIdList());
        if (CollUtil.isEmpty(stationIds)) {
            throw new BmosException(MesResponseCode.CAN_NOT_CHOSE_THIS_EQUIPMENT);
        }
        return result;
    }
}
