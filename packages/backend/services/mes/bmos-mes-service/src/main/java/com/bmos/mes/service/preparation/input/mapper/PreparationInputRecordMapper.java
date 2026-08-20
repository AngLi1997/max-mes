package com.bmos.mes.service.preparation.input.mapper;

import com.bmos.mes.service.preparation.input.model.PreparationInputRecord;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 配液投入记录表(BmPreparationInputRecord)表数据库访问层
 *
 * @author makejava
 * @since 2024-08-01 12:54:45
 */
@Mapper
public interface PreparationInputRecordMapper extends BaseMapperX<PreparationInputRecord> {

    /**
     * 查询当前配液单下哪些已经投料
     * @param preparationPlanIdList
     * @return
     */
    default List<PreparationInputRecord> selectByPlanIdList(Collection<Long> preparationPlanIdList){
        return selectList(new LambdaQueryWrapperX<PreparationInputRecord>()
                .in(PreparationInputRecord::getPreparationPlanId, preparationPlanIdList));
    }

    /**
     * 查询当前配液单是否有投料记录
     * @param preparationPlanId
     * @return
     */
    default boolean existByPlanId(Long preparationPlanId){
        return exists(new LambdaQueryWrapperX<PreparationInputRecord>()
                .eq(PreparationInputRecord::getPreparationPlanId, preparationPlanId));
    }

    /**
     * 根据物料件id查询投入记录
     * @param storageMaterialIdList
     * @return
     */
    default List<PreparationInputRecord> selectByStorageMaterialIdList(Collection<Long> storageMaterialIdList){
        return selectList(new LambdaQueryWrapperX<PreparationInputRecord>()
                .in(PreparationInputRecord::getStorageMaterialId, storageMaterialIdList));
    }

}

