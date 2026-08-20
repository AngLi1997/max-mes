package com.bmos.mes.service.ingredient.input.service;

import com.bmos.mes.service.ingredient.input.dto.IngredientInputDTO;
import com.bmos.mes.service.ingredient.input.dto.InputComponentInstanceQueryDTO;
import com.bmos.mes.service.ingredient.input.dto.PendingInputPlanListQueryListDTO;
import com.bmos.mes.service.ingredient.input.vo.IngredientInputPlanVO;
import com.bmos.mes.service.ingredient.input.vo.InputComponentInstanceVO;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientPlanItemVO;
import com.bmos.mes.service.storage.manage.vo.StorageMaterialVO;
import com.bmos.mes.service.tag.dto.ScanWeighMaterialCodeWithIngredientPlanId;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/26 10:16
 */
public interface IIngredientInputService {


    /**
     * 根据配料单id查询物料件批次
     *
     * @param ingredientPlanId
     * @return
     */
    @Nullable
    IngredientInputPlanVO queryInputListByPlanId(Long ingredientPlanId, Long componentInstanceId);

    /**
     * 投料
     *
     * @param dto
     */
    void input(IngredientInputDTO dto);

    /**
     * 扫描物料件号查询物料件信息（校验配料单信息）
     * @param scanQuery
     * @return
     */
    StorageMaterialVO scanWeighMaterialCodeWithIngredientPlanId(ScanWeighMaterialCodeWithIngredientPlanId scanQuery);

    /**
     * 查询未投料完成的配料单列表
     * @return
     */
    List<IngredientPlanItemVO> queryPendingInputPlanList(PendingInputPlanListQueryListDTO dto);

    InputComponentInstanceVO getInputComponentInstance(InputComponentInstanceQueryDTO dto);
}
