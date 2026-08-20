package com.bmos.mes.service.inspect.convert;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.user.SysUser;
import com.bmos.mes.common.enums.inpspect.InspectProgramResultEnum;
import com.bmos.mes.common.enums.inpspect.InspectStatusEnum;
import com.bmos.mes.inspect.dto.InspectProgramResultDTO;
import com.bmos.mes.inspect.dto.InspectResultCallBackDTO;
import com.bmos.mes.inspect.dto.InspectResultItemDTO;
import com.bmos.mes.service.formula.model.ProductFormulaMaterial;
import com.bmos.mes.service.inspect.controller.vo.*;
import com.bmos.mes.service.inspect.model.Inspect;
import com.bmos.mes.service.inspect.model.InspectInfo;
import com.bmos.mes.service.inspect.model.InspectResult;
import com.bmos.mes.service.inspect.service.dto.InitiateInspectDTO;
import com.bmos.mes.service.inspect.service.dto.InitiateInspectInfoDTO;
import com.bmos.mes.service.plan.info.model.Plan;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface InspectConvert {

    InspectConvert INSTANCE = Mappers.getMapper(InspectConvert.class);


    default Inspect convert2Inspect(InitiateInspectDTO dto, Plan plan,
                                    ProductFormulaMaterial productFormulaMaterial, SysUser user){
        Inspect inspect = new Inspect();
        inspect.setInspectNo(dto.getInspectNo());
        inspect.setStatus(InspectStatusEnum.PENDING);
        inspect.setInspectorId(user.getUserId());
        inspect.setInspector(StrUtil.format("{}-{}", user.getUserName(), user.getLoginName()));
        inspect.setInspectTime(LocalDateTime.now());
        inspect.setProcedureModelId(dto.getProcedureModelId());
        inspect.setProcedureStepModelId(dto.getProcedureStepModelId());
        inspect.setProcessChangeNumber(dto.getProcessChangeNumber());
        inspect.setProcedureChangeNumber(dto.getProcedureChangeNumber());
        inspect.setInspectConfigId(dto.getInspectConfigId());
        inspect.setFormulaMaterialId(productFormulaMaterial.getId());
        inspect.setMaterialId(productFormulaMaterial.getMaterialId());
        inspect.setMaterialType(productFormulaMaterial.getMaterialType());
        inspect.setMaterialBatchNo(dto.getMaterialBatchNo());
        inspect.setMaterialMergeCode(productFormulaMaterial.getMaterialMergeCode());
        inspect.setMaterialName(productFormulaMaterial.getMaterialName());
        inspect.setPlanId(plan.getId());
        inspect.setProductName(plan.getProductName());
        inspect.setProductMergeCode(plan.getProductMergeCode());
        inspect.setPlanNo(plan.getPlanNo());
        inspect.setBatchNo(plan.getBatchNo());
        inspect.setBatchQuantity(plan.getBatchQuantity());
        inspect.setUnitId(plan.getUnitId());
        return inspect;
    }

    default List<InspectInfo> convert2InspectInfoList(List<InitiateInspectInfoDTO> initiateInspectInfoDTOList, Inspect inspect){
        if (CollUtil.isEmpty(initiateInspectInfoDTOList)){
            return new ArrayList<>();
        }
        return initiateInspectInfoDTOList.stream().map(item -> {
            InspectInfo inspectInfo = new InspectInfo();
            inspectInfo.setInspectId(inspect.getId());
            inspectInfo.setInspectConfigDataId(item.getInspectConfigDataId());
            inspectInfo.setCode(item.getCode());
            inspectInfo.setShowName(item.getShowName());
            inspectInfo.setDataName(item.getDataName());
            inspectInfo.setRequired(item.getRequired());
            inspectInfo.setValue(item.getValue());
            inspectInfo.setSort(item.getSort());
            return inspectInfo;
        }).collect(Collectors.toList());
    }

    List<InspectPageVO> convert2PageVO(List<Inspect> inspects);

    InspectDetailVO convert2InspectDetailVO(Inspect inspect);

    List<InspectInfoVO> convert2InspectInfoVOList(List<InspectInfo> inspectInfos);

    default List<InspectResult> convert2InspectResultList(List<InspectProgramResultDTO> inspectProgramResultDTOList, Inspect inspect){
        if (CollUtil.isEmpty(inspectProgramResultDTOList)){
            return new ArrayList<>();
        }
        List<InspectResult> inspectResults = new ArrayList<>();
        for (InspectProgramResultDTO inspectProgramResultDTO : inspectProgramResultDTOList) {

        }
        return inspectResults;
    }

    InspectResultVO convert2InspectResultVO(Inspect inspect);

    List<InspectProgramResultVO> convert2InspectResultVOList(List<InspectResult> inspectResultList);

    default InspectResult convert2InspectResult(InspectResultItemDTO inspectResultItemDTO, Inspect inspect){
        InspectResult inspectResult = new InspectResult();
        inspectResult.setInspectId(inspect.getId());
        inspectResult.setInspectProgramNo(inspectResultItemDTO.getInspectProgramNo());
        inspectResult.setInspectProgramName(inspectResultItemDTO.getInspectProgramName());
        inspectResult.setInspectResult(inspectResultItemDTO.getInspectResult());
        inspectResult.setInspectDictNo(inspectResultItemDTO.getAlreadyConvertProgramNo());
        inspectResult.setInspectConclusion(CommonEnum.getEnumByName(InspectProgramResultEnum.class, inspectResultItemDTO.getInspectConclusion()));
        return  inspectResult;
    }
}
