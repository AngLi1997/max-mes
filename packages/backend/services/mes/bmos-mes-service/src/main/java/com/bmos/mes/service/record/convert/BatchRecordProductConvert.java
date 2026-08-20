package com.bmos.mes.service.record.convert;

import com.bmos.mes.service.product.dto.RecordSaveDTO;
import com.bmos.mes.service.record.dto.ProductSaveDTO;
import com.bmos.mes.service.record.model.BatchRecord;
import com.bmos.mes.service.record.model.BatchRecordProduct;
import com.bmos.mes.service.record.vo.ProductRecordTreeVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface BatchRecordProductConvert {
    BatchRecordProductConvert INSTANCE = Mappers.getMapper(BatchRecordProductConvert.class);

    default List<BatchRecordProduct> convertToList(ProductSaveDTO dto) {
        List<BatchRecordProduct> list = dto.getProductIdList()
                .stream()
                .map(productId -> {
                    BatchRecordProduct product = new BatchRecordProduct();
                    product.setRecordId(dto.getRecordId());
                    product.setProductId(productId);
                    return product;
                }).collect(Collectors.toList());
        return list;
    }

    default List<BatchRecordProduct> convertToList(RecordSaveDTO dto) {
        List<BatchRecordProduct> list = dto.getRecordIds().stream().map(recordId -> {
            BatchRecordProduct batchRecordProduct = new BatchRecordProduct();
            batchRecordProduct.setRecordId(recordId);
            batchRecordProduct.setProductId(dto.getProductId());
            return batchRecordProduct;
        }).collect(Collectors.toList());
        return list;
    }

    List<ProductRecordTreeVO> convertToProductRecordTreeVO(List<BatchRecord> batchRecords);
}
