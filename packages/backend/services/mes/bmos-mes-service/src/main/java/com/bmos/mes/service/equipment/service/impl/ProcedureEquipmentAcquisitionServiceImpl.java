package com.bmos.mes.service.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.equipment.mapper.ProcedureEquipmentAcquisitionMapper;
import com.bmos.mes.service.equipment.mapper.entity.ProcedureEquipmentAcquisition;
import com.bmos.mes.service.equipment.service.ProcedureEquipmentAcquisitionService;
import com.bmos.mes.service.equipment.service.dto.EquipmentAcquisitionComponentDTO;
import com.bmos.mes.service.equipment.service.enums.EquipmentAcquisitionComponentInputTypeEnum;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.enums.ExecuteFormDataType;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.service.BatchRecordComponentService;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.platform.facade.equipment.feign.EquipmentConfigFeign;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author yigaohui
 * @date 2024/4/23
 **/
@Service
public class ProcedureEquipmentAcquisitionServiceImpl extends ServiceImpl<ProcedureEquipmentAcquisitionMapper,
        ProcedureEquipmentAcquisition> implements ProcedureEquipmentAcquisitionService {


    @Resource
    private EquipmentConfigFeign equipmentConfigFeign;

    @Resource
    private BatchRecordComponentService recordComponentService;

    @Autowired
    private ProcedureStepConfigService procedureStepConfigService;

    @Autowired
    private ProcedureStepModelService procedureStepModelService;

    @Autowired
    private ExecuteFormDataService executeFormDataService;

    @Autowired
    private Map<String, BusinessComponentStrategy> businessComponentStrategyMap;

    /**
     * 设备数采点位组件信息保存
     *
     * @param equipmentAcquisitionComponentDTO 数采组件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEquipmentAcquisitionComponent(EquipmentAcquisitionComponentDTO equipmentAcquisitionComponentDTO) {
        ComponentListVO componentListVO = this.getComponent(equipmentAcquisitionComponentDTO);
        EquipmentInfoFeignVO equipmentInfo = getEquipmentInfo(equipmentAcquisitionComponentDTO.getEquipmentId());
        List<ProcedureEquipmentAcquisition> acquisitions = this.saveEquipmentAcquisitionData(equipmentAcquisitionComponentDTO, equipmentInfo);
        this.saveExecuteFormData(equipmentAcquisitionComponentDTO, componentListVO, acquisitions, equipmentInfo);
    }

    private ComponentListVO getComponent(EquipmentAcquisitionComponentDTO equipmentAcquisitionComponentDTO) {
        ComponentListVO componentListVO =
                recordComponentService.selectUsedComponentDetail(equipmentAcquisitionComponentDTO.getRecordVersionId(),
                        equipmentAcquisitionComponentDTO.getRecordItemId(),
                        equipmentAcquisitionComponentDTO.getComponentId());

        BusinessComponentTypeEnum enumByValue =
                BusinessComponentTypeEnum.getEnumByValue(componentListVO.getComponentType());
        if (BusinessComponentTypeEnum.EQUIPMENT_DATA_ACQUISITION != enumByValue) {
            throw new BmosException(MesResponseCode.EQUIPMENT_ACQUISITION_COMPONENT_TYPE_ERROR);
        }
        return componentListVO;
    }

    private List<ProcedureEquipmentAcquisition> saveEquipmentAcquisitionData(EquipmentAcquisitionComponentDTO equipmentAcquisitionComponentDTO, EquipmentInfoFeignVO equipmentInfo) {
        List<ProcedureEquipmentAcquisition> list = baseMapper.selectComponentAcquisitionDataList(equipmentAcquisitionComponentDTO);
        int maxSort = list.stream().mapToInt(ProcedureEquipmentAcquisition::getAcquisitionSort).max().orElse(0);
        List<ProcedureEquipmentAcquisition> equipmentAcquisitions =
                equipmentAcquisitionComponentDTO.getEquipmentAcquisitionPoint().stream().map(item -> {
                    ProcedureEquipmentAcquisition procedureEquipmentAcquisition =
                            BeanUtil.toBean(equipmentAcquisitionComponentDTO, ProcedureEquipmentAcquisition.class);
                    procedureEquipmentAcquisition.setId(IdUtils.getSnowflake());
                    procedureEquipmentAcquisition.setAcquisitionId(item.getAcquisitionId());
                    procedureEquipmentAcquisition.setAcquisitionCode(item.getAcquisitionCode());
                    procedureEquipmentAcquisition.setDataPointName(item.getDataPointName());
                    procedureEquipmentAcquisition.setDataPointValue(item.getDataPointValue());
                    if (item.getInputType() == EquipmentAcquisitionComponentInputTypeEnum.MANUAL) {
                        procedureEquipmentAcquisition.setDataPointValueTime(LocalDateTime.now());
                    } else {
                        procedureEquipmentAcquisition.setDataPointValueTime(item.getDataPointValueTime());
                    }
                    procedureEquipmentAcquisition.setAcquisitionTime(LocalDateTime.now());
                    procedureEquipmentAcquisition.setInputType(item.getInputType());
                    procedureEquipmentAcquisition.setAcquisitionSort(maxSort + 1);
                    procedureEquipmentAcquisition.setReuse(equipmentAcquisitionComponentDTO.getReuse());
                    procedureEquipmentAcquisition.setEquipmentName(equipmentInfo.getName());
                    procedureEquipmentAcquisition.setEquipmentCode(equipmentInfo.getCode());
                    procedureEquipmentAcquisition.setGroupComponentId(equipmentAcquisitionComponentDTO.getEquipmentAcquisitionGroupComponentId());
                    procedureEquipmentAcquisition.setDataDictCode(item.getDataPropertyCode());
                    return procedureEquipmentAcquisition;
                }).collect(Collectors.toList());
        this.saveBatch(equipmentAcquisitions);
        list.addAll(equipmentAcquisitions);
        return list;
    }

    /**
     * 修改和保存走同一逻辑，数据留痕
     *
     * @param equipmentAcquisitionComponentDTO 数采组件信息
     */
    @Override
    public void modifyEquipmentAcquisitionComponent(EquipmentAcquisitionComponentDTO equipmentAcquisitionComponentDTO) {
        ComponentListVO componentListVO = this.getComponent(equipmentAcquisitionComponentDTO);
        EquipmentInfoFeignVO equipmentInfo = getEquipmentInfo(equipmentAcquisitionComponentDTO.getEquipmentId());
        List<ProcedureEquipmentAcquisition> acquisitions = this.saveEquipmentAcquisitionData(equipmentAcquisitionComponentDTO, equipmentInfo);
        this.modifyExecuteFormData(equipmentAcquisitionComponentDTO, componentListVO, acquisitions, equipmentInfo);
    }

    private void modifyExecuteFormData(EquipmentAcquisitionComponentDTO equipmentAcquisitionComponentDTO, ComponentListVO componentListVO,
                                       List<ProcedureEquipmentAcquisition> acquisitions, EquipmentInfoFeignVO equipmentInfo) {
        List<ExecuteFormData> res = this.generateExecuteFormData(equipmentAcquisitionComponentDTO, componentListVO, acquisitions, equipmentInfo);
        res.forEach(e -> {
            e.setOperationType(Objects.equals(e.getComponentType(), BusinessComponentTypeEnum.EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE.getValue()) ?
                    ExecuteFormDataType.UPDATE.getValue() : ExecuteFormDataType.MODIFY.getValue());
            e.setOperationUser(SysUserHolder.getUser().getUserId());
            e.setOperationTime(LocalDateTime.now());
            e.setRemark(equipmentAcquisitionComponentDTO.getRemark());
        });
        executeFormDataService.saveResultsAndHandleRelationWithExceptionRecord(res,
                equipmentAcquisitionComponentDTO.getProductPlanId(),
                equipmentAcquisitionComponentDTO.getComponentId(),
                equipmentAcquisitionComponentDTO.getProcedureStepModelId(),
                equipmentAcquisitionComponentDTO.getCopyVersion());
    }

    private void saveExecuteFormData(EquipmentAcquisitionComponentDTO equipmentAcquisitionComponentDTO,
                                     ComponentListVO componentListVO, List<ProcedureEquipmentAcquisition> acquisitions,
                                     EquipmentInfoFeignVO equipmentInfo) {
        List<ExecuteFormData> res = this.generateExecuteFormData(equipmentAcquisitionComponentDTO, componentListVO, acquisitions, equipmentInfo);
        res.forEach(e -> {
            e.setOperationType(ExecuteFormDataType.SAVE.getValue());
            e.setOperationUser(SysUserHolder.getUser().getUserId());
            e.setOperationTime(LocalDateTime.now());
        });
        if (CollUtil.isNotEmpty(res)) {
            executeFormDataService.saveResultsAndHandleRelationWithExceptionRecord(res,
                    equipmentAcquisitionComponentDTO.getProductPlanId(),
                    equipmentAcquisitionComponentDTO.getComponentId(),
                    equipmentAcquisitionComponentDTO.getProcedureStepModelId(),
                    equipmentAcquisitionComponentDTO.getCopyVersion());
        }
    }

    private List<ExecuteFormData> generateExecuteFormData(EquipmentAcquisitionComponentDTO equipmentInfoComponentDTO,
                                                          ComponentListVO componentListVO, List<ProcedureEquipmentAcquisition> acquisitions, EquipmentInfoFeignVO equipmentInfo) {
        ArrayList<ExecuteFormData> res = Lists.newArrayList();
        List<ComponentListVO> children = componentListVO.getChildren();
        if (org.apache.commons.collections4.CollectionUtils.isEmpty(children)) {
            return res;
        }
        Long procedureStepModelId = equipmentInfoComponentDTO.getProcedureStepModelId();
        ProcedureStepModel procedureStepModel = procedureStepModelService.getById(procedureStepModelId);
        List<BusinessComponentConfigDetailVO> configs =
                procedureStepConfigService.getComponentConfigByProcedureStepModel(procedureStepModel);
        Map<Long, BusinessComponentConfigDetailVO> configMap = CollectionUtils.convertMap(configs,
                BusinessComponentConfigDetailVO::getComponentId);
        ProductionDetailInfo productionDetailInfo = this.configContext(equipmentInfoComponentDTO, acquisitions, equipmentInfo);
        children.forEach(item -> {
            if (!item.getId().equals(equipmentInfoComponentDTO.getEquipmentAcquisitionGroupComponentId())) {
                return;
            }
            // 设备数采组件放在根节点上的,将自己的配置全部替换成根节点的配置
            configMap.put(item.getId(), configMap.get(componentListVO.getId()));
            businessComponentStrategyMap.get(item.getComponentType()).handleBusinessComponent(res, item,
                    productionDetailInfo, configMap, null);
        });
        return res;
    }

    private ProductionDetailInfo configContext(EquipmentAcquisitionComponentDTO equipmentInfoComponentDTO, List<ProcedureEquipmentAcquisition> acquisitions,
                                               EquipmentInfoFeignVO equipmentInfo) {
        BusinessComponentBatchSaveDTO businessComponentBatchSaveDTO = BeanUtil.toBean(equipmentInfoComponentDTO,
                BusinessComponentBatchSaveDTO.class);
        return new ProductionDetailInfo().setEquipmentInfo(equipmentInfo)
                .setDto(businessComponentBatchSaveDTO)
                .setEquipmentAcquisitionPointList(equipmentInfoComponentDTO.getEquipmentAcquisitionPoint())
                .setAcquisitionTime(equipmentInfoComponentDTO.getAcquisitionTime())
                .setEquipmentAcquisitionPointList(equipmentInfoComponentDTO.getEquipmentAcquisitionPoint()).setEquipmentAcquisitionList(acquisitions);
    }

    private EquipmentInfoFeignVO getEquipmentInfo(Long equipmentId) {
        ResponseInfo<EquipmentInfoFeignVO> equipmentInfoFeignVOResponseInfo =
                equipmentConfigFeign.getConfigByEquipmentId(equipmentId);
        if (equipmentInfoFeignVOResponseInfo == null || equipmentInfoFeignVOResponseInfo.getData() == null) {
            throw new BmosException(MesResponseCode.EQUIPMENT_INFO_COMPONENT_EQUIPMENT_NOT_EXITS_ERROR);
        }
        return equipmentInfoFeignVOResponseInfo.getData();
    }

}
