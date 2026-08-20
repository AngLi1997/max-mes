package com.bmos.mes.service.ingredient.plan.convert;

import com.bmos.mes.service.ingredient.plan.dto.BindMaterialBatchDTO;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatch;
import com.bmos.mes.service.ingredient.plan.model.IngredientMaterialBatchDetailInfo;
import com.bmos.mes.service.ingredient.plan.vo.AvailableAndBoundMaterialBatchVO;
import com.bmos.mes.service.ingredient.plan.vo.IngredientBoundMaterialBatchVO;
import com.bmos.mes.service.storage.manage.vo.ReservedBatchInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface IngredientPlanConverter {

    IngredientPlanConverter INSTANCE = Mappers.getMapper(IngredientPlanConverter.class);

    default List<IngredientBoundMaterialBatchVO> convertDetailToBoundMaterialBatchVO(List<IngredientMaterialBatchDetailInfo> list){
        return list.stream().map(this::convertDetailToBoundMaterialBatchVO).collect(Collectors.toList());
    }

    @Mapping(target = "originalBatchNo",source = "factoryBatchNo")
    @Mapping(target = "originalCode",source = "originalBatchNo")
    IngredientBoundMaterialBatchVO convertDetailToBoundMaterialBatchVO(IngredientMaterialBatchDetailInfo info);


    IngredientMaterialBatch convertToIngredientMaterialBatch(BindMaterialBatchDTO e);

    List<AvailableAndBoundMaterialBatchVO> convertToBoundMaterialBatchVOList(List<ReservedBatchInfo> reservedBatchInfos);
}
