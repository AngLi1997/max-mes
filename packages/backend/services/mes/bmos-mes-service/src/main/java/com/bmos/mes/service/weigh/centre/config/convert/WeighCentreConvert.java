package com.bmos.mes.service.weigh.centre.config.convert;

import com.bmos.mes.service.weigh.centre.config.model.WeighCentre;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreCategoryWithCentreVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentreDetailVO;
import com.bmos.mes.service.weigh.centre.config.vo.WeighCentrePageVO;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 15:39
 */
@Mapper
public interface WeighCentreConvert {

    WeighCentreConvert INSTANCE = Mappers.getMapper(WeighCentreConvert.class);

    WeighCentreDetailVO convertToVO(WeighCentre weighCentre);

    CommonPage<WeighCentrePageVO> convertToVO(CommonPage<WeighCentre> page);

    @Mapping(target = "isCategory", expression = "java(false)")
    @Mapping(target = "name", expression = "java(weighCentre.getCode() + '-' + weighCentre.getName())")
    WeighCentreCategoryWithCentreVO convertCategoryNodeVO(WeighCentre weighCentre);

    List<WeighCentreCategoryWithCentreVO> convertCategoryNodeVO(List<WeighCentre> list);
}
