package com.bmos.mes.service.preparation.produce.mapper;

import com.bmos.mes.service.preparation.produce.model.PreparationProduceRecord;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 配液产出称量记录表(BmPreparationProduceRecord)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-01 12:56:04
 */
@Mapper
public interface PreparationProduceRecordMapper extends BaseMapperX<PreparationProduceRecord> {

    /**
     * 根据进度id查询记录
     * @param progressId
     * @return
     */
    default List<PreparationProduceRecord> selectByProgressId(Long progressId){
        return selectList(new LambdaQueryWrapperX<PreparationProduceRecord>()
                .eq(PreparationProduceRecord::getProcedureProduceProgressId, progressId));
    }

    /**
     * 根据签名状态查询
     * @param progressId
     * @param signStatus
     * @return
     */
    default List<PreparationProduceRecord> selectBySignStatus(Long progressId, Integer signStatus){
        return selectList(new LambdaQueryWrapperX<PreparationProduceRecord>()
                .eq(PreparationProduceRecord::getProcedureProduceProgressId, progressId)
                .eq(PreparationProduceRecord::getSignStatus, signStatus));
    }

    /**
     * 根据物料件id查询配液产出记录
     * @param storageMaterialId
     * @return
     */
    default PreparationProduceRecord selectByStorageMaterialId(Long storageMaterialId){
        return selectOne(new LambdaQueryWrapperX<PreparationProduceRecord>()
                .eq(PreparationProduceRecord::getStorageMaterialId, storageMaterialId));
    }
}

