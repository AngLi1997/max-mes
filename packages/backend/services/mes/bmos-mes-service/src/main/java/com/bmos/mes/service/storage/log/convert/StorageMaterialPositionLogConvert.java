package com.bmos.mes.service.storage.log.convert;

import com.bmos.mes.service.storage.log.model.StorageMaterialPositionLog;
import com.bmos.mes.service.storage.log.vo.StorageMaterialPositionLogVO;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/21 09:46
 */
@Mapper
public interface StorageMaterialPositionLogConvert {

    StorageMaterialPositionLogConvert INSTANCE = Mappers.getMapper(StorageMaterialPositionLogConvert.class);

    CommonPage<StorageMaterialPositionLogVO> convertPage(CommonPage<StorageMaterialPositionLog> page);

    @Mapping(target = "quantity", expression = "java(com.bmos.mes.service.utils.BigDecimalFormatUtil.formatBigDecimal(log.getQuantity()))")
    StorageMaterialPositionLogVO convertToVO(StorageMaterialPositionLog log);

}
