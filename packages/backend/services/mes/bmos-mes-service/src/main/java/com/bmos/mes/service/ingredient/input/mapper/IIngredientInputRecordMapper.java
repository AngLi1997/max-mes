package com.bmos.mes.service.ingredient.input.mapper;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.service.ingredient.input.model.IngredientInputRecord;
import com.bmos.mes.service.ingredient.input.model.IngredientInputRecordDetail;
import com.bmos.mes.service.ingredient.input.vo.IngredientInputRecordVO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/26 10:20
 */
@Mapper
public interface IIngredientInputRecordMapper extends BaseMapperX<IngredientInputRecord> {

    /**
     * 根据配料单id查询投料列表
     *
     * @param ingredientPlanId 配料单id
     * @param materialIds      物料id列表
     * @return 投料列表
     */
    List<IngredientInputRecordVO> queryInputListByPlanId(@Param("ingredientPlanId") Long ingredientPlanId, @Param("materialIds") List<Long> materialIds);

    /**
     * 根据配料单id查询已投料数据
     *
     * @param ingredientPlanId 配料单id
     * @return
     */
    default List<IngredientInputRecord> queryInputedListByPlanId(@Param("ingredientPlanId") Long ingredientPlanId) {
        return selectList(Wrappers.lambdaQuery(IngredientInputRecord.class)
                .eq(IngredientInputRecord::getIngredientPlanId, ingredientPlanId)
        );
    }

    List<IngredientInputRecordDetail> queryInputedDetailListByPlanId(@Param("ingredientPlanId") Long id);

    List<IngredientInputRecordDetail> queryInputedDetailListByComponentInstanceId(@Param("componentInstanceId") Long id);
}
