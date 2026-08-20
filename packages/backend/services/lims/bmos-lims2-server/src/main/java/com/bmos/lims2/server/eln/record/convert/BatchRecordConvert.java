package com.bmos.lims2.server.eln.record.convert;

import com.bmos.lims2.server.eln.record.entity.BatchRecord;
import com.bmos.lims2.server.eln.record.vo.ProductRecordTreeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BatchRecordConvert {
    BatchRecordConvert INSTANCE = Mappers.getMapper(BatchRecordConvert.class);
    List<ProductRecordTreeVO> convertToProductRecordTreeVO(List<BatchRecord> batchRecords);
}
