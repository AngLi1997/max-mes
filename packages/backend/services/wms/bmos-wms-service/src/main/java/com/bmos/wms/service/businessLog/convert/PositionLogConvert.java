package com.bmos.wms.service.businessLog.convert;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.businessLog.model.PositionLog;
import com.bmos.wms.service.businessLog.vo.PositionLogVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/10 16:13
 */
@Mapper
public interface PositionLogConvert {

    PositionLogConvert INSTANCE = Mappers.getMapper(PositionLogConvert.class);

    CommonPage<PositionLogVO> convertToVO(CommonPage<PositionLog> page);
}
