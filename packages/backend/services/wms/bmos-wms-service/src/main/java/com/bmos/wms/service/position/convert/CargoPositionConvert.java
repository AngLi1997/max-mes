package com.bmos.wms.service.position.convert;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.position.model.CargoPosition;
import com.bmos.wms.service.position.vo.CargoPositionVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 15:35
 */
@Mapper
public interface CargoPositionConvert {

    CargoPositionConvert INSTANCE = Mappers.getMapper(CargoPositionConvert.class);

    CargoPositionVO convertToVO(CargoPosition cargoPosition);

    CommonPage<CargoPositionVO> convertToPageVO(CommonPage<CargoPosition> page);
}
