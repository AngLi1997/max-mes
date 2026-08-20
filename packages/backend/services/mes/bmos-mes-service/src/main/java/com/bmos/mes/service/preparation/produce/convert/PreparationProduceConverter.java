package com.bmos.mes.service.preparation.produce.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.preparation.plan.model.LiquidPreparationPlan;
import com.bmos.mes.service.preparation.produce.controller.vo.*;
import com.bmos.mes.service.preparation.produce.model.PreparationProduceProgress;
import com.bmos.mes.service.preparation.produce.model.PreparationProduceRecord;
import com.bmos.mes.service.preparation.produce.service.dto.ProduceConfirmUserDTO;
import com.bmos.mes.service.process.model.ProcedureStepModel;
import com.bmos.mes.service.record.business.model.preparation.PreparationProduceMaterialInfo;
import com.bmos.mes.service.storage.config.model.CargoPosition;
import com.bmos.mes.service.storage.manage.model.StorageMaterialBatch;
import com.bmos.mes.service.tag.vo.ScanCargoPositionVO;
import com.bmos.mes.service.tag.vo.ScanDeviceVO;
import com.bmos.platform.facade.equipment.vo.EquipmentInfoFeignVO;
import com.bmos.platform.facade.system.user.vo.FeignUserVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.*;
import java.util.stream.Collectors;

@Mapper
public interface PreparationProduceConverter {

    PreparationProduceConverter INSTANCE = Mappers.getMapper(PreparationProduceConverter.class);

    default PreparationProduceProgressVO convert2PreparationProduceProgressVO(PreparationProduceProgress preparationProduceProgress, Map<String, FeignUserVO> userMap){
        if (Objects.isNull(preparationProduceProgress)){
            return null;
        }
        PreparationProduceProgressVO vo = new PreparationProduceProgressVO();
        vo.setProgressId(preparationProduceProgress.getId());
        vo.setReCheckerId(preparationProduceProgress.getReCheckerId());
        vo.setReCheckerName(userMap.get(preparationProduceProgress.getReCheckerId()).getUserName());
        vo.setReCheckerLoginName(userMap.get(preparationProduceProgress.getReCheckerId()).getLoginName());
        vo.setProducerId(preparationProduceProgress.getProducerId());
        vo.setProducerName(userMap.get(preparationProduceProgress.getProducerId()).getUserName());
        vo.setProducerLoginName(userMap.get(preparationProduceProgress.getProducerId()).getLoginName());
        return vo;
    }

    default PreparationProducePlanVO convert2ProducePlanVO(LiquidPreparationPlan preparationPlan){
        if (Objects.isNull(preparationPlan)){
            return null;
        }
        PreparationProducePlanVO vo = new PreparationProducePlanVO();
        vo.setId(preparationPlan.getId());
        vo.setName(preparationPlan.getName());
        return vo;
    }

    default PreparationProduceMaterialBatchVO convert2PreparationProduceMaterialBatchVO(ProductFormulaMaterial formulaMaterial, String unitName){

        PreparationProduceMaterialBatchVO vo = new PreparationProduceMaterialBatchVO();
        vo.setMaterialName(formulaMaterial.getMaterialName());
        vo.setMaterialMergeCode(formulaMaterial.getMaterialMergeCode());
        vo.setMaterialSpecification(formulaMaterial.getMaterialSpecification());
        vo.setUnitId(formulaMaterial.getUnitId());
        vo.setUnit(unitName);
        return vo;
    }

    default PreparationProduceMaterialBatchVO convert2PreparationProduceMaterialBatchVO(StorageMaterialBatch storageMaterialBatch,
                                                                                        ProductFormulaMaterial formulaMaterial, String unitName){
        PreparationProduceMaterialBatchVO vo = new PreparationProduceMaterialBatchVO();
        if (Objects.nonNull(storageMaterialBatch)){
            vo.setMaterialBatchId(storageMaterialBatch.getId());
            vo.setMaterialBatchNo(storageMaterialBatch.getMaterialBatchNo());
            vo.setExpireDate(storageMaterialBatch.getExpiredDate());
        }
        vo.setFormulaMaterialId(formulaMaterial.getId());
        vo.setMaterialName(formulaMaterial.getMaterialName());
        vo.setMaterialMergeCode(formulaMaterial.getMaterialMergeCode());
        vo.setMaterialSpecification(formulaMaterial.getMaterialSpecification());
        vo.setUnitId(formulaMaterial.getUnitId());
        vo.setUnit(unitName);
        return vo;
    }

    default List<PreparationProducePlanVO> convert2ProducePlanVOList(List<LiquidPreparationPlan> preparationPlanList){
        if (Objects.isNull(preparationPlanList)){
            return Collections.emptyList();
        }
        return preparationPlanList.stream().map(this::convert2ProducePlanVO).collect(Collectors.toList());
    }

    default PreparationProduceMaterialVO convertProduceMaterialVO(ProductFormulaMaterial formulaMaterial){
        if (Objects.isNull(formulaMaterial)){
            return null;
        }
        PreparationProduceMaterialVO vo = new PreparationProduceMaterialVO();
        vo.setFormulaMaterialId(formulaMaterial.getId());
        vo.setMaterialName(formulaMaterial.getMaterialName());
        vo.setMaterialMergeCode(formulaMaterial.getMaterialMergeCode());
        vo.setMaterialSpecification(formulaMaterial.getMaterialSpecification());
        vo.setUnitId(formulaMaterial.getUnitId());
        return vo;
    }

    default List<PreparationProduceUserVO> convertReCheckUserVOList(List<FeignUserVO> authFeignUserVOS){
        if (CollUtil.isEmpty(authFeignUserVOS)) {
            return Collections.emptyList();
        }
        List<PreparationProduceUserVO> userVOList = new ArrayList<>();
        for (FeignUserVO authFeignUserVO : authFeignUserVOS) {
            PreparationProduceUserVO userVO = new PreparationProduceUserVO();
            userVO.setUserId(authFeignUserVO.getUserId());
            userVO.setLoginName(authFeignUserVO.getLoginName());
            userVO.setShowName(authFeignUserVO.getLoginName() + StrUtil.DASHED + authFeignUserVO.getUserName());
            userVO.setUserName(authFeignUserVO.getUserName());
            userVOList.add(userVO);
        }
        return userVOList;
    }

    default PreparationProduceProgress convert2Progress(ProduceConfirmUserDTO dto){
        PreparationProduceProgress progress = new PreparationProduceProgress();
        progress.setProductPlanId(dto.getProductPlanId());
        progress.setProcedureStepModelId(dto.getProcedureStepModelId());
        progress.setComponentId(dto.getComponentId());
        progress.setCopyVersion(dto.getCopyVersion());
        progress.setReuse(dto.getReuse());
        progress.setPreparationPlanId(dto.getPreparationPlanId());
        progress.setProducerId(dto.getConfirmUserId());
        progress.setReCheckerId(dto.getReCheckUserId());
        progress.setRemark(dto.getRemark());
        progress.setRecordItemId(dto.getRecordItemId());
        progress.setRecordVersionId(dto.getRecordVersionId());
        progress.setFormulaMaterialId(dto.getFormulaMaterialId());
        progress.setMaterialBatchNo(dto.getMaterialBatchNo());
        progress.setExpiredDate(dto.getExpireDate());
        return progress;
    }

    default ProduceVO convert2ProduceVO(PreparationProduceProgress preparationProduceProgress,
                                        Map<String, FeignUserVO> userVOMap){
        if (Objects.isNull(preparationProduceProgress)){
            return null;
        }
        ProduceVO produceVO = new ProduceVO();
        produceVO.setId(preparationProduceProgress.getId());
        produceVO.setProductPlanId(preparationProduceProgress.getProductPlanId());
        produceVO.setProcedureStepModelId(preparationProduceProgress.getProcedureStepModelId());
        produceVO.setCopyVersion(preparationProduceProgress.getCopyVersion());
        produceVO.setComponentId(preparationProduceProgress.getComponentId());
        produceVO.setProducerId(preparationProduceProgress.getProducerId());
        FeignUserVO producerVO = userVOMap.get(preparationProduceProgress.getProducerId());
        if (Objects.nonNull(producerVO)){
            produceVO.setProducerName(producerVO.getUserName());
            produceVO.setProducerLoginName(producerVO.getLoginName());
        }
        produceVO.setReCheckerId(preparationProduceProgress.getReCheckerId());
        FeignUserVO reCheckerVO = userVOMap.get(preparationProduceProgress.getReCheckerId());
        if (Objects.nonNull(reCheckerVO)){
            produceVO.setReCheckerName(reCheckerVO.getUserName());
            produceVO.setReCheckerLoginName(reCheckerVO.getLoginName());
        }
        produceVO.setFormulaMaterialId(preparationProduceProgress.getFormulaMaterialId());
        produceVO.setStorageMaterialBatchNo(preparationProduceProgress.getMaterialBatchNo());
        produceVO.setExpiredDate(preparationProduceProgress.getExpiredDate());
        return produceVO;
    }

    default List<ProduceRecordVO> convert2ProduceRecordVOList(List<PreparationProduceRecord> preparationProduceRecordList,
                                                              PreparationProduceProgress preparationProduceProgress,
                                                              ProductFormulaMaterial formulaMaterial){
        if (CollUtil.isEmpty(preparationProduceRecordList)){
            return Collections.emptyList();
        }
        List<ProduceRecordVO> produceRecordVOList = new ArrayList<>();

        return  produceRecordVOList;
    }

    default ScanDeviceVO convertScanDeviceVO(EquipmentInfoFeignVO equipmentInfoFeignVO){
        if (Objects.isNull(equipmentInfoFeignVO)){
            return null;
        }
        ScanDeviceVO scanDeviceVO = new ScanDeviceVO();
        scanDeviceVO.setDeviceCode(equipmentInfoFeignVO.getCode());
        scanDeviceVO.setDeviceId(equipmentInfoFeignVO.getId());
        scanDeviceVO.setDeviceName(equipmentInfoFeignVO.getName());
        return scanDeviceVO;
    }

    default ScanCargoPositionVO convertCargoPositionVO(CargoPosition cargoPosition){
        if (Objects.isNull(cargoPosition)){
            return null;
        }
        ScanCargoPositionVO scanCargoPositionVO = new ScanCargoPositionVO();
        scanCargoPositionVO.setCode(cargoPosition.getCode());
        scanCargoPositionVO.setFullName(cargoPosition.getCode() + StrUtil.DASHED + cargoPosition.getPosition());
        scanCargoPositionVO.setId(cargoPosition.getId());
        scanCargoPositionVO.setName(cargoPosition.getPosition());
        return scanCargoPositionVO;
    }

    BusinessComponentBatchSaveDTO convert2BaseDTO(BusinessDataHandleBaseDTO businessDataHandleBaseDTO);

    List<PreparationProduceMaterialInfo> convert2DetailInfo(List<ProduceRecordVO> produceRecordList);

    BusinessDataHandleBaseDTO convert2HandleBaseDTO(PreparationProduceProgress progress);

    default BusinessDataHandleBaseDTO convertToBusinessDataBaseDTO(LiquidPreparationPlan liquidPreparationPlan, ProcedureStepModel procedureStepModel){
        BusinessDataHandleBaseDTO result = convertToBusinessDataBaseDTO(liquidPreparationPlan);
        result.setProcedureStepId(procedureStepModel.getProcedureStepId());
        result.setProcessId(procedureStepModel.getProcessId());
        result.setProcessVersion(procedureStepModel.getProcessVersion());
        return result;
    }

    BusinessDataHandleBaseDTO convertToBusinessDataBaseDTO(LiquidPreparationPlan liquidPreparationPlan);
}
