package com.bmos.mes.service.preparation.input.convert;

import cn.hutool.core.collection.CollUtil;
import com.bmos.mes.common.enums.preparation.PrepareSignStatusEnum;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputComponentInstanceVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputPlanVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationInputRecordVO;
import com.bmos.mes.service.preparation.input.controller.vo.PreparationPlanItemVO;
import com.bmos.mes.service.preparation.input.model.PreparationInputComponentInstance;
import com.bmos.mes.service.preparation.input.model.PreparationInputRecord;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputBindPlanDTO;
import com.bmos.mes.service.preparation.input.service.dto.PreparationInputDTO;
import com.bmos.mes.service.preparation.measure.model.LiquidPreparationMeasureRecord;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.record.business.model.preparation.PreparationInputMaterialInfo;
import com.bmos.mes.service.storage.manage.model.StorageMaterial;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.mes.service.tag.vo.ScanInputMaterialVO;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.*;

@Mapper
public interface PreparationInputConvert {

    PreparationInputConvert INSTANCE = Mappers.getMapper(PreparationInputConvert.class);

    default PreparationInputComponentInstanceVO convert2InstanceVO(PreparationInputComponentInstance componentInstance,
                                                                   LiquidPreparationPlan liquidPreparationPlan){
        PreparationInputComponentInstanceVO vo = new PreparationInputComponentInstanceVO();
        vo.setComponentInstanceId(componentInstance.getId());
        vo.setPlanId(liquidPreparationPlan.getId());
        vo.setPlanName(liquidPreparationPlan.getName());
        vo.setComplete(componentInstance.getComplete());
        return vo;
    }

    List<PreparationPlanItemVO> convert2PlanItemVOList(List<LiquidPreparationPlan> planList);

    default List<PreparationPlanItemVO> convert2PlanItemVOList(List<LiquidPreparationPlan> planList, Set<Long> alreadyPlanIdList){
        if (CollUtil.isEmpty(planList)){
            return Collections.emptyList();
        }
        List<PreparationPlanItemVO> voList = new ArrayList<>();
        for (LiquidPreparationPlan liquidPreparationPlan : planList) {
            if (alreadyPlanIdList.contains(liquidPreparationPlan.getId())){
                continue;
            }
            voList.add(convert2PlanItemVO(liquidPreparationPlan));
        }
        return voList;
    }

    PreparationPlanItemVO convert2PlanItemVO(LiquidPreparationPlan liquidPreparationPlan);

    default PreparationInputComponentInstance convert2ComponentInstance(PreparationInputBindPlanDTO dto, ProcedureStepModel procedureStepModel){
        PreparationInputComponentInstance componentInstance = new PreparationInputComponentInstance();
        componentInstance.setComponentId(dto.getComponentId());
        componentInstance.setCopyVersion(dto.getCopyVersion());
        componentInstance.setPreparationPlanId(dto.getPreparationPlanId());
        componentInstance.setProcedureStepModelId(procedureStepModel.getId());
        componentInstance.setProductPlanId(dto.getProductPlanId());
        componentInstance.setRecordItemId(procedureStepModel.getRecordItemId());
        componentInstance.setRecordVersionId(procedureStepModel.getRecordVersionId());
        componentInstance.setReuse(procedureStepModel.getReusable());
        componentInstance.setComplete(Boolean.FALSE);
        return componentInstance;
    }

    default PreparationInputPlanVO convert2InputPlanVO(LiquidPreparationPlan liquidPreparationPlan){
        if (Objects.isNull(liquidPreparationPlan)){
            return null;
        }
        PreparationInputPlanVO vo = new PreparationInputPlanVO();
        vo.setPreparePlanId(liquidPreparationPlan.getId());
        vo.setPreparePlanName(liquidPreparationPlan.getName());
        vo.setProductPlanId(liquidPreparationPlan.getProductPlanId());
        return vo;
    }

    default PreparationInputRecordVO convert2InputRecordVO(LiquidPreparationMeasureRecord measureRecord){
        PreparationInputRecordVO vo = new PreparationInputRecordVO();
        // 在下方书写一段代码。将PreparationInputRecordVO中的所有属性进行复制
        vo.setStorageMaterialId(measureRecord.getStorageMaterialId());
        vo.setStorageMaterialNo(measureRecord.getStorageMaterialNo());
        vo.setStorageMaterialBatchId(measureRecord.getStorageMaterialBatchId());
        vo.setStorageMaterialBatchNo(measureRecord.getStorageMaterialBatchNo());
        vo.setMaterialName(measureRecord.getMaterialName());
        vo.setMaterialMergeCode(measureRecord.getMaterialMergeCode());
        vo.setUnitId(measureRecord.getUnitId());
        vo.setFormulaMaterialId(measureRecord.getFormulaMaterialId());
        vo.setQuantity(measureRecord.getQuantity());
        return vo;
    }

    default ScanDeviceVO convert2ScanDeviceVO(EquipmentInfoFeignVO equipmentInfoFeignVO){
        ScanDeviceVO scanDeviceVO = new ScanDeviceVO();
        scanDeviceVO.setDeviceId(equipmentInfoFeignVO.getId());
        scanDeviceVO.setDeviceName(equipmentInfoFeignVO.getName());
        scanDeviceVO.setDeviceCode(equipmentInfoFeignVO.getCode());
        return scanDeviceVO;
    }

    default List<PreparationInputRecord> convert2InputRecordVOList(List<PreparationInputRecordVO> inputRecordVOList,
                                                                   PreparationInputComponentInstance componentInstance,
                                                                   PreparationInputDTO dto,
                                                                   EquipmentInfoFeignVO equipmentInfoFeignVO,
                                                                   Integer sort) {
        List<PreparationInputRecord> inputRecordList = new ArrayList<>();
        if (CollUtil.isEmpty(inputRecordVOList)){
            return inputRecordList;
        }
        LocalDateTime now = LocalDateTime.now();
        for (PreparationInputRecordVO inputRecordVO : inputRecordVOList) {
            PreparationInputRecord inputRecord = new PreparationInputRecord();
            inputRecord.setStorageMaterialId(inputRecordVO.getStorageMaterialId());
            inputRecord.setStorageMaterialNo(inputRecordVO.getStorageMaterialNo());
            inputRecord.setStorageMaterialBatchId(inputRecordVO.getStorageMaterialBatchId());
            inputRecord.setStorageMaterialBatchNo(inputRecordVO.getStorageMaterialBatchNo());
            inputRecord.setFormulaMaterialId(inputRecordVO.getFormulaMaterialId());
            inputRecord.setQuantity(String.valueOf(inputRecordVO.getQuantity()));
            inputRecord.setUnitId(inputRecordVO.getUnitId());
            inputRecord.setComponentInstanceId(componentInstance.getId());
            inputRecord.setPreparationPlanId(componentInstance.getPreparationPlanId());
            inputRecord.setDeviceId(equipmentInfoFeignVO.getId());
            inputRecord.setDeviceCode(equipmentInfoFeignVO.getCode());
            inputRecord.setDeviceName(equipmentInfoFeignVO.getName());
            inputRecord.setInputTime(now);
            inputRecord.setImporterId(dto.getInputUserId());
            inputRecord.setRemark(dto.getRemark());
            inputRecord.setSort(sort);
            inputRecord.setSignStatus(PrepareSignStatusEnum.SIGNED.getValue());
            sort++;
            inputRecordList.add(inputRecord);
        }
        return inputRecordList;
    }

    List<PreparationInputMaterialInfo> convert2StorageMaterialInfoList(List<PreparationInputRecordVO> inputRecordVO);

    BusinessComponentBatchSaveDTO convert2BaseDTO(PreparationInputDTO dto);

    ScanInputMaterialVO convert2ScanInputMaterialVO(StorageMaterial storageMaterial);
}
