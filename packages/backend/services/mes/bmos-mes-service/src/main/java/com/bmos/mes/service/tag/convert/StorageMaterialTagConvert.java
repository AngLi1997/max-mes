package com.bmos.mes.service.tag.convert;

import com.bmos.mes.service.tag.vo.BaseStorageMaterialTag;
import com.bmos.mes.service.tag.vo.PreparationProduceStorageMaterialTag;
import com.bmos.mes.service.tag.vo.StorageMaterialTag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/7 16:57
 */
@Mapper
public interface StorageMaterialTagConvert {

    StorageMaterialTagConvert INSTANCE = Mappers.getMapper(StorageMaterialTagConvert.class);

    StorageMaterialTag convert(BaseStorageMaterialTag baseStorageMaterialTag);

    PreparationProduceStorageMaterialTag convert2Produce(BaseStorageMaterialTag baseStorageMaterialTag);
}
