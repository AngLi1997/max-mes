package com.bmos.mes.service.record.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.logging.annotation.OperationLog;
import com.bmos.mes.service.product.dto.RecordSaveDTO;
import com.bmos.mes.service.record.convert.BatchRecordProductConvert;
import com.bmos.mes.service.record.dto.ProductSaveDTO;
import com.bmos.mes.service.record.mapper.BatchRecordProductMapper;
import com.bmos.mes.service.record.model.BatchRecordProduct;
import com.bmos.mes.service.record.service.BatchRecordProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class BatchRecordProductServiceImpl implements BatchRecordProductService {

    @Autowired
    private BatchRecordProductMapper productMapper;

    @Override
    @OperationLog
    public Boolean saveProduct(ProductSaveDTO dto) {
        productMapper.deleteByRecordId(dto.getRecordId());
        List<BatchRecordProduct> list = BatchRecordProductConvert.INSTANCE.convertToList(dto);
        return productMapper.saveProduct(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindBatchRecords(RecordSaveDTO dto) {
        productMapper.deleteByProductId(dto.getProductId());
        if(CollUtil.isNotEmpty(dto.getRecordIds())){
            List<BatchRecordProduct> list = BatchRecordProductConvert.INSTANCE.convertToList(dto);
            productMapper.saveProduct(list);
        }
    }

    @Override
    public List<Long> getProductBindRecordIds(Long productId) {
        List<BatchRecordProduct> list = productMapper.selectByProductIds(productId);
        return CollectionUtils.convertList(list, BatchRecordProduct::getRecordId);
    }

    @Override
    public List<Long> queryProductIdByRecordId(Long recordId) {
        List<BatchRecordProduct> list = productMapper.queryProductIdByRecordId(recordId);
        if (CollUtil.isEmpty(list)){
            return Collections.emptyList();
        }
        return CollectionUtils.convertList(list, BatchRecordProduct::getProductId);
    }
}
