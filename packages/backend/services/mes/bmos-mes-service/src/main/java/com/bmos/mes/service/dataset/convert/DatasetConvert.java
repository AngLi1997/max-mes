package com.bmos.mes.service.dataset.convert;

import com.bmos.mes.service.dataset.model.Dataset;
import com.bmos.mes.service.dataset.vo.DatasetSimpleVO;
import com.bmos.mes.service.dataset.vo.DatasetVO;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 13:50
 */
@Mapper
public interface DatasetConvert {

    DatasetConvert INSTANCE = Mappers.getMapper(DatasetConvert.class);

    List<DatasetSimpleVO> convertToVO(List<Dataset> datasets);

    CommonPage<DatasetSimpleVO> convertToVO(CommonPage<Dataset> datasets);

    DatasetVO convertToVO(Dataset dataset);
}
