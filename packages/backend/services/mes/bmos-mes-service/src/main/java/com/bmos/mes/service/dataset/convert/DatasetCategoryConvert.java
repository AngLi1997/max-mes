package com.bmos.mes.service.dataset.convert;

import com.bmos.mes.service.dataset.model.DatasetCategory;
import com.bmos.mes.service.dataset.vo.DatasetCategoryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 13:50
 */
@Mapper
public interface DatasetCategoryConvert {
    DatasetCategoryConvert INSTANCE = Mappers.getMapper(DatasetCategoryConvert.class);

    List<DatasetCategoryVO> convertToVO(List<DatasetCategory> datasetCategories);
}
