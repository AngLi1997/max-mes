package com.bmos.wms.service.businessLog.convert;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.businessLog.model.CargoLog;
import com.bmos.wms.service.businessLog.vo.CargoLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/10 16:13
 */
@Mapper
public interface CargoLogConvert {

    CargoLogConvert INSTANCE = Mappers.getMapper(CargoLogConvert.class);

    CommonPage<CargoLogVO> convertToVO(CommonPage<CargoLog> page);
}
