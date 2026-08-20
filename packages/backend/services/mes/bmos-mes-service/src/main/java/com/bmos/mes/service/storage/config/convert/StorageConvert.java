package com.bmos.mes.service.storage.config.convert;

import com.bmos.mes.service.storage.config.model.Storage;
import com.bmos.mes.service.storage.config.vo.StorageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/2/18 15:35
 */
@Mapper
public interface StorageConvert {
    StorageConvert INSTANCE = Mappers.getMapper(StorageConvert.class);

    StorageVO convertVO(Storage storage);

    List<StorageVO> convertVO(List<Storage> list);
}
