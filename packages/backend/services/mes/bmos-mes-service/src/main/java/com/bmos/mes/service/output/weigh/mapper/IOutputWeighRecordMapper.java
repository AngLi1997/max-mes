package com.bmos.mes.service.output.weigh.mapper;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import com.bmos.mes.service.output.weigh.model.OutputWeighRecord;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/28 15:36
 */
@Mapper
public interface IOutputWeighRecordMapper extends BaseMapperX<OutputWeighRecord> {

    default List<OutputWeighRecord> queryRecordListByProcessId(Long outputWeighProcessId) {
        if (outputWeighProcessId == null) {
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(OutputWeighRecord.class)
                .eq(OutputWeighRecord::getOutputWeighProcessId, outputWeighProcessId)
        );
    }

    default List<OutputWeighRecord> scrapBatch(List<Long> scrapStorageMaterialIdList) {
        update(null, Wrappers.lambdaUpdate(OutputWeighRecord.class)
                .in(OutputWeighRecord::getStorageMaterialId, scrapStorageMaterialIdList)
                .set(OutputWeighRecord::getSignStatus, WeighSignStatus.SCRAPED)
                .set(OutputWeighRecord::getContainerId, null)
                .set(OutputWeighRecord::getContainerName, null)
                .set(OutputWeighRecord::getMaterialPositionId, null)
        );
        return selectList(Wrappers.lambdaQuery(OutputWeighRecord.class)
                .in(OutputWeighRecord::getStorageMaterialId, scrapStorageMaterialIdList)
        );
    }

    default OutputWeighRecord queryByStorageMaterialId(Long id){
        return selectOne(Wrappers.lambdaQuery(OutputWeighRecord.class)
                .eq(OutputWeighRecord::getStorageMaterialId, id)
        );
    }

    default List<OutputWeighRecord> queryRecordListByProcessIds(List<Long> processIds){
        if (CollectionUtil.isEmpty(processIds)){
            return new ArrayList<>();
        }
        return selectList(Wrappers.lambdaQuery(OutputWeighRecord.class)
                .in(OutputWeighRecord::getOutputWeighProcessId, processIds)
        );
    }
}
