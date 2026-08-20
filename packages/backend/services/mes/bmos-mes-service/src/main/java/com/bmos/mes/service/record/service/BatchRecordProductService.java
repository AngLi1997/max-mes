package com.bmos.mes.service.record.service;

import com.bmos.mes.service.product.dto.RecordSaveDTO;
import com.bmos.mes.service.record.dto.ProductSaveDTO;

import java.util.List;

public interface BatchRecordProductService {


    Boolean saveProduct(ProductSaveDTO dto);

    void bindBatchRecords(RecordSaveDTO dto);

    List<Long> getProductBindRecordIds(Long productId);

    List<Long> queryProductIdByRecordId(Long recordId);
}
