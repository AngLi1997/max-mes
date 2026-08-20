package com.bmos.mes.service.equipment.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bmos.common.exception.BmosException;
import com.bmos.common.holder.SysUserHolder;
import com.bmos.common.response.ResponseInfo;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.id.IdUtils;
import com.bmos.mes.common.enums.record.BusinessComponentTypeEnum;
import com.bmos.mes.common.exception.MesResponseCode;
import com.bmos.mes.service.equipment.mapper.ProcedureEquipmentInfoMapper;
import com.bmos.mes.service.equipment.mapper.entity.ProcedureEquipmentInfo;
import com.bmos.mes.service.equipment.service.ProcedureEquipmentInfoComponentService;
import com.bmos.mes.service.equipment.service.dto.EquipmentInfoComponentDTO;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.enums.ExecuteFormDataType;
import com.bmos.mes.service.execute.mapper.ExecuteFormDataMapper;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.service.ExecuteFormDataService;
import com.bmos.mes.service.execute.service.ExecuteRecordCopyService;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.process.service.ProcedureStepConfigService;
import com.bmos.mes.service.process.service.ProcedureStepModelService;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.business.strategy.EquipmentCustomFieldComponentStrategy;
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

/**
 * @author yigaohui
 * @date 2024/4/23
 **/
@Service
public class ProcedureEquipmentInfoComponentServiceImpl extends ServiceImpl<ProcedureEquipmentInfoMapper,
        ProcedureEquipmentInfo> implements ProcedureEquipmentInfoComponentService {

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
    private ExecuteFormDataMapper executeFormDataMapper;

    @Autowired
    private ExecuteRecordCopyService executeRecordCopyService;

    @Autowired
    private Map<String, BusinessComponentStrategy> businessComponentStrategyMap;

    @Autowired
    private EquipmentCustomFieldComponentStrategy equipmentCustomFieldComponentStrategy;

    /**
     * 1. 查到设备信息
     * 2. 找到组件的配置
     * 3. 将设备的字段与子组件对应，生成业务数据和表单的数据
     *
     * @param equipmentInfoComponentDTO 设备信息组件数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveEquipmentInfoComponent(EquipmentInfoComponentDTO equipmentInfoComponentDTO) {
        ComponentListVO componentListVO = getComponent(equipmentInfoComponentDTO);
        this.saveExecuteFormData(equipmentInfoComponentDTO, componentListVO);
        this.saveEquipmentData(equipmentInfoComponentDTO);
    }

    private void saveEquipmentData(EquipmentInfoComponentDTO equipmentInfoComponentDTO) {
        ProcedureEquipmentInfo procedureEquipmentInfo = BeanUtil.toBean(equipmentInfoComponentDTO,
                ProcedureEquipmentInfo.class);
        procedureEquipmentInfo.setId(IdUtils.getSnowflake());
        this.save(procedureEquipmentInfo);
    }

    private ComponentListVO getComponent(EquipmentInfoComponentDTO equipmentInfoComponentDTO) {
        ComponentListVO componentListVO =
                recordComponentService.selectUsedComponentDetail(equipmentInfoComponentDTO.getRecordVersionId(),
                        equipmentInfoComponentDTO.getRecordItemId(), equipmentInfoComponentDTO.getComponentId());

        BusinessComponentTypeEnum enumByValue =
                BusinessComponentTypeEnum.getEnumByValue(componentListVO.getComponentType());
        if (BusinessComponentTypeEnum.EQUIPMENT_INFO != enumByValue) {
            throw new BmosException(MesResponseCode.EQUIPMENT_INFO_COMPONENT_TYPE_ERROR);
        }
        return componentListVO;
    }

    private void saveExecuteFormData(EquipmentInfoComponentDTO equipmentInfoComponentDTO,
                                     ComponentListVO componentListVO) {
        List<ExecuteFormData> res = this.generateExecuteFormData(equipmentInfoComponentDTO, componentListVO);
        res.forEach(e -> {
            e.setOperationType(ExecuteFormDataType.SAVE.getValue());
            e.setOperationUser(SysUserHolder.getUser().getUserId());
            e.setOperationTime(LocalDateTime.now());
        });
        if (CollUtil.isNotEmpty(res)) {
            executeFormDataService.saveResultsAndHandleRelationWithExceptionRecord(res,
                    equipmentInfoComponentDTO.getProductPlanId(),
                    equipmentInfoComponentDTO.getComponentId(),
                    equipmentInfoComponentDTO.getProcedureStepModelId(),
                    equipmentInfoComponentDTO.getCopyVersion());
        }
    }


    /**
     * 设备信息组件数据修改,修改和保存走同一逻辑，数据留痕
     *
     * @param equipmentInfoComponentDTO 组件信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void modifyEquipmentInfoComponent(EquipmentInfoComponentDTO equipmentInfoComponentDTO) {
        ComponentListVO componentListVO = getComponent(equipmentInfoComponentDTO);
        this.modifyExecuteFormData(equipmentInfoComponentDTO, componentListVO);
        this.saveEquipmentData(equipmentInfoComponentDTO);
    }

    private void modifyExecuteFormData(EquipmentInfoComponentDTO equipmentInfoComponentDTO, ComponentListVO componentListVO) {
        List<ExecuteFormData> res = this.generateExecuteFormData(equipmentInfoComponentDTO, componentListVO);
        res.forEach(e -> {
            e.setOperationType(ExecuteFormDataType.MODIFY.getValue());
            e.setOperationUser(SysUserHolder.getUser().getUserId());
            e.setOperationTime(LocalDateTime.now());
            e.setRemark(equipmentInfoComponentDTO.getRemark());
        });
        if (CollUtil.isNotEmpty(res)) {
            executeFormDataService.saveResultsAndHandleRelationWithExceptionRecord(res,
                    equipmentInfoComponentDTO.getProductPlanId(),
                    equipmentInfoComponentDTO.getComponentId(),
                    equipmentInfoComponentDTO.getProcedureStepModelId(),
                    equipmentInfoComponentDTO.getCopyVersion());
        }
    }




    private List<ExecuteFormData> generateExecuteFormData(EquipmentInfoComponentDTO equipmentInfoComponentDTO,
                                                          ComponentListVO componentListVO) {
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
        ProductionDetailInfo productionDetailInfo = this.configContext(equipmentInfoComponentDTO);
        children.forEach(item -> {
            if (BusinessComponentTypeEnum.CUSTOM_FIELD.getValue().equals(item.getComponentType())) {
                equipmentCustomFieldComponentStrategy.handleBusinessComponent(res, item,
                        productionDetailInfo, configMap, null);
            } else {
                businessComponentStrategyMap.get(item.getComponentType()).handleBusinessComponent(res, item,
                        productionDetailInfo, configMap, null);
            }
        });
        return res;
    }

    private ProductionDetailInfo configContext(EquipmentInfoComponentDTO equipmentInfoComponentDTO) {
        ResponseInfo<EquipmentInfoFeignVO> equipmentInfoFeignVOResponseInfo =
                equipmentConfigFeign.getConfigByEquipmentId(equipmentInfoComponentDTO.getEquipmentId());
        if (equipmentInfoFeignVOResponseInfo == null || equipmentInfoFeignVOResponseInfo.getData() == null) {
            throw new BmosException(MesResponseCode.EQUIPMENT_INFO_COMPONENT_EQUIPMENT_NOT_EXITS_ERROR);
        }
        BusinessComponentBatchSaveDTO businessComponentBatchSaveDTO = BeanUtil.toBean(equipmentInfoComponentDTO,
                BusinessComponentBatchSaveDTO.class);
        return new ProductionDetailInfo().setEquipmentInfo(equipmentInfoFeignVOResponseInfo.getData()).setDto(businessComponentBatchSaveDTO);
    }

}
