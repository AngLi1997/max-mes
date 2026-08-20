package com.bmos.mes.service.mcp.convert;

import com.bmos.mes.service.mcp.vo.WmsStorageInventoryDataVO;
import com.bmos.mes.service.mcp.vo.WmsStorageInventoryFeignVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2025/4/24 20:53
 */
@Mapper
public interface WmsStorageInventoryFeignConverter {
    WmsStorageInventoryFeignConverter INSTANCE = Mappers.getMapper(WmsStorageInventoryFeignConverter.class);

    WmsStorageInventoryDataVO convertToData(WmsStorageInventoryFeignVO feignVO);

    List<WmsStorageInventoryDataVO> convertToData(List<WmsStorageInventoryFeignVO> list);
}
