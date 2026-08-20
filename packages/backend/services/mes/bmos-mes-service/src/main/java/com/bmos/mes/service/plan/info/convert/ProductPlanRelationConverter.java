package com.bmos.mes.service.plan.info.convert;

import com.bmos.mes.common.enums.BooleanEnum;
import com.bmos.mes.service.plan.info.model.ProductPlanRelation;
import com.bmos.mes.service.plan.info.vo.PlanRetraceMaterialPageVO;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelationTreeNodeVO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamProductStartConfirmDTO;
import com.bmos.mes.service.trace.material.entity.MaterialTraceHistoryDO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductPlanRelationConverter {
    ProductPlanRelationConverter INSTANCE = Mappers.getMapper(ProductPlanRelationConverter.class);

    default List<ProductPlanRelation> convertList(InstructionTeamProductStartConfirmDTO dto) {
        return dto.getRelationPlan().stream()
            .map(detail -> detail.getPlanIds()
                .stream()
                .map(planId -> ProductPlanRelation.builder()
                    .productPlanId(dto.getPlanId())
                    .relationProductPlanId(planId)
                    .processId(detail.getProcessId())
                    .isDirectRelation(BooleanEnum.TRUE)
                    .build()
                ).collect(Collectors.toList())
            )
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    default PlanRetraceMaterialPageVO convertMaterial(MaterialTraceHistoryDO materialTraceHistoryDO){
        if (materialTraceHistoryDO == null){
            return null;
        }
        PlanRetraceMaterialPageVO planRetraceMaterialPageVO = new PlanRetraceMaterialPageVO();
        planRetraceMaterialPageVO.setMaterialName(materialTraceHistoryDO.getMaterialName());
        planRetraceMaterialPageVO.setMaterialCode(materialTraceHistoryDO.getMergeCode());
        planRetraceMaterialPageVO.setMaterialSpecification(materialTraceHistoryDO.getMaterialSpecification());
        planRetraceMaterialPageVO.setStorageMaterialNo(materialTraceHistoryDO.getStorageMaterialNo());
        planRetraceMaterialPageVO.setStorageMaterialBatchNo(materialTraceHistoryDO.getStorageMaterialBatchNo());
        if (materialTraceHistoryDO.getQuantity() != null){
            planRetraceMaterialPageVO.setMaterialQuantity(materialTraceHistoryDO.getQuantity().toPlainString());
        }
        planRetraceMaterialPageVO.setUnitName(materialTraceHistoryDO.getUnitName());
        if (materialTraceHistoryDO.getOperateTime() != null){
            planRetraceMaterialPageVO.setOperationTime(materialTraceHistoryDO.getOperateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        planRetraceMaterialPageVO.setOperationType(materialTraceHistoryDO.getOperateType().getName());
        planRetraceMaterialPageVO.setOperateUserName(UserUtils.getUsername(materialTraceHistoryDO.getOperateUserId()));
        return planRetraceMaterialPageVO;
    }

    CommonPage<PlanRetraceMaterialPageVO> convertToMaterialPage(CommonPage<MaterialTraceHistoryDO> materialTraceHistoryDO);

    List<ProductPlanRelationTreeNodeVO> convertToVO(List<ProductPlanRelation> list);
}
