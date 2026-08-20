package com.bmos.wms.service.log.convert;

import com.bmos.common.base.enums.CommonEnum;
import com.bmos.wms.service.log.model.WmsLogModel;
import com.bmos.wms.service.log.vo.WmsLogDetailVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WmsLogConvert {

    WmsLogConvert INSTANCE = Mappers.getMapper(WmsLogConvert.class);

    @Mapping(target = "operationType", expression = "java(com.bmos.common.base.enums.CommonEnum.getEnumByValue(com.bmos.logging.enums.OperationTypeEnum.class, wmsLogModel.getOperationType()))")
    WmsLogDetailVO convert2Detail(WmsLogModel wmsLogModel);
}
