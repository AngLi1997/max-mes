package com.bmos.wms.service.sendout.convert;

import com.bmos.mybatis.page.CommonPage;
import com.bmos.wms.service.sendout.model.SendOutOrder;
import com.bmos.wms.service.sendout.vo.SendOutOrderDetailVO;
import com.bmos.wms.service.sendout.vo.SendOutOrderVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/16 09:20
 */
@Mapper
public interface SendOutOrderConvert {

    SendOutOrderConvert INSTANCE = Mappers.getMapper(SendOutOrderConvert.class);

    CommonPage<SendOutOrderVO> convertToVO(CommonPage<SendOutOrder> page);

    SendOutOrderDetailVO convertToDetailVO(SendOutOrder sendOutOrder);
}
