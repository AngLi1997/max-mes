package com.bmos.mes.service.ingredient.weigh.convert;

import com.bmos.mes.service.ingredient.weigh.dto.WeighLogSaveDTO;
import com.bmos.mes.service.ingredient.weigh.model.IngredientWeighProcess;
import com.bmos.mes.service.ingredient.weigh.model.WeighLog;
import com.bmos.mes.service.ingredient.weigh.vo.IngredientWeighProcessVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/22 18:08
 */
@Mapper
public interface IngredientWeighProcessConvert {

    IngredientWeighProcessConvert INSTANCE = Mappers.getMapper(IngredientWeighProcessConvert.class);

    IngredientWeighProcessVO convertToVO(IngredientWeighProcess process);

    WeighLog convertToLog(WeighLogSaveDTO dto);
}
