package com.bmos.mes.service.plan.team.service;

import com.bmos.mes.service.facotry.controller.vo.FactoryLineInfoVO;
import com.bmos.mes.service.plan.team.dto.*;
import com.bmos.mes.service.plan.team.vo.ProductPlanPageTeamVO;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamDetailVO;
import com.bmos.mes.service.plan.team.vo.ProductPlanTeamListVO;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface ProductPlanTeamService {
    /**
     * 分页查询
     * @param dto dto
     * @return List<ProductPlanPageTeamVO>
     */
    List<ProductPlanPageTeamVO> page(ProductPlanTeamPageDTO dto);

    ProductPlanTeamDetailVO detail(Long id);

    /**
     * 列表查询
     * @param dto dto
     * @return List<ProductPlanPageTeamVO>
     */
    List<ProductPlanPageTeamVO> list(ProductPlanTeamListDTO dto);

    /**
     * 保存
     * @param dto dto
     */
    void save(ProductPlanTeamSaveDTO dto);

    /**
     * 更新
     * @param dto dto
     */
    void update(ProductPlanTeamUpdateDTO dto);

    /**
     * 启用
     * @param id id
     */
    void enable(Long id);

    /**
     * 停用
     * @param id id
     */
    void disable(Long id);


    List<Long> getListByUserId(String userId);

    List<ProductPlanTeamListVO> getTeamListByProductionLineIds(List<Long> split);

    List<ProductPlanTeamListVO> getTeamListByProductPlanId(Long productPlanId);

    List<ProductPlanTeamListVO> getTeamListByProcessVersionId(Long processVersionId);

    void boundProductionLines(TeamBoundProductionLineDTO dto);

    List<FactoryLineInfoVO> listLinesByTeamId(Long teamId);

    List<ProductPlanTeamListVO> getProcessTeamListByProductionLineIds(List<Long> lineIds, Long processVersionId);

    List<ProductPlanTeamListVO> getStepTeamListByProcessVersionId(Long processVersionId, Long procedureModelId);
}
