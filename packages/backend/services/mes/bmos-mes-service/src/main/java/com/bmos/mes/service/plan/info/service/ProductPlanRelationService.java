package com.bmos.mes.service.plan.info.service;

import com.bmos.mes.service.plan.info.model.ProductPlanRelation;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelatedProcessVO;
import com.bmos.mes.service.plan.info.vo.ProductPlanRelationListVO;
import com.bmos.mes.service.plan.team.dto.InstructionTeamProductStartConfirmDTO;

import java.util.List;

public interface ProductPlanRelationService {
    /**
     * 关联关系保存
     * @param dto dto
     */
    void save(InstructionTeamProductStartConfirmDTO dto);

    List<ProductPlanRelation> getList(Long productPlanId);

    List<ProductPlanRelationListVO> detail(Long productPlanId);

    List<ProductPlanRelationListVO> detailWithSelf(Long productPlanId);

    /**
     * 保存生产计划关联批次并做其他更新处理
     * @param relations
     * @param planId
     */
    void saveProductPlanRelation(List<ProductPlanRelation> relations, Long planId);

    /**
     * 更新生产计划关联批次
     * @param relations
     * @param id
     */
    void updateProductPlanRelation(List<ProductPlanRelation> relations, Long id);

    /**
     * 查询生产计划关联工艺及其下批次列表
     * @param planId
     * @return
     */
    List<ProductPlanRelatedProcessVO> queryProductPlanRelationList(Long planId);

}
