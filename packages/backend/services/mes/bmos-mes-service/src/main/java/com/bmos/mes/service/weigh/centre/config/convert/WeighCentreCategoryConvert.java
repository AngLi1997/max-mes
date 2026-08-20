package com.bmos.mes.service.weigh.centre.config.convert;

import com.bmos.mes.service.weigh.centre.config.model.WeighCentreCategory;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryWithCentreVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 15:39
 */
@Mapper
public interface WeighCentreCategoryConvert {

    WeighCentreCategoryConvert INSTANCE = Mappers.getMapper(WeighCentreCategoryConvert.class);

    List<WeighCentreCategoryVO> convertToVO(List<WeighCentreCategory> weighCentreCategories);

    List<WeighCentreCategoryWithCentreVO> convertToVOWithCentre(List<WeighCentreCategory> weighCentreCategories);

    WeighCentreCategoryVO convertToVO(WeighCentreCategory weighCentreCategory);
}
