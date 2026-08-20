package com.bmos.mes.service.dataset.convert;

import com.bmos.mes.service.dataset.model.DatasetPoint;
import com.bmos.mes.service.dataset.vo.DatasetDynamicReportDataVO;
import com.bmos.mes.service.dataset.vo.DatasetLotReleaseLinkVO;
import com.bmos.mes.service.dataset.vo.DatasetPointPageVO;
import com.bmos.mes.service.dataset.vo.DatasetPointVO;
import com.bmos.mybatis.page.CommonPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/23 13:50
 */
@Mapper
public interface DatasetPointConvert {

    DatasetPointConvert INSTANCE = Mappers.getMapper(DatasetPointConvert.class);

    CommonPage<DatasetPointPageVO> convertToVO(CommonPage<DatasetPoint> datasetPoints);

    List<DatasetPointVO> convertToPointVO(List<DatasetPoint> datasetPoints);

    @Mapping(target = "dataName", source = "name")
    DatasetDynamicReportDataVO convertToDynamicReportVO(DatasetPoint datasetPoint);

    List<DatasetDynamicReportDataVO> convertToDynamicReportVO(List<DatasetPoint> datasetPoints);

    List<DatasetLotReleaseLinkVO> convertToLotReleaseLinkVO(List<DatasetPoint> datasetPoints);
}
