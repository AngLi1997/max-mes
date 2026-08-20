package com.bmos.mes.service.weigh.centre.execute.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.centre.execute.model.WeighExecuteConsumeRecord;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 19:03
 */
@Mapper
public interface IWeighExecuteConsumeRecordMapper extends BaseMapperX<WeighExecuteConsumeRecord> {

    /**
     * 根据需求id和物料批次id查询称量消耗记录
     * @param requirementId 需求id
     * @param storageMaterialBatchId 物料批次id
     * @return 称量消耗记录
     */
    default List<WeighExecuteConsumeRecord> selectListByRequirementIdAndBatchId(Long requirementId, Long storageMaterialBatchId){
        if (requirementId == null || storageMaterialBatchId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteConsumeRecord>()
                .eq(WeighExecuteConsumeRecord::getRequirementId, requirementId)
                .eq(WeighExecuteConsumeRecord::getStorageMaterialBatchId, storageMaterialBatchId)
        );
    }

    /**
     * 根据需求id查询称量消耗记录
     * @param requirementId 需求id
     * @return 称量消耗记录
     */
    default List<WeighExecuteConsumeRecord> selectListByRequirementId(Long requirementId){
        if (requirementId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteConsumeRecord>()
                .eq(WeighExecuteConsumeRecord::getRequirementId, requirementId)
        );
    }

    default List<WeighExecuteConsumeRecord> selectListByTaskId(Long taskId){
        if (taskId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteConsumeRecord>()
                .eq(WeighExecuteConsumeRecord::getTaskId, taskId)
        );
    }

    default List<WeighExecuteConsumeRecord> selectListByTaskIdAndBatchId(Long taskId, Long batchId){
        if (taskId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteConsumeRecord>()
                .eq(WeighExecuteConsumeRecord::getTaskId, taskId)
                .eq(WeighExecuteConsumeRecord::getStorageMaterialBatchId, batchId)
        );
    }

}
