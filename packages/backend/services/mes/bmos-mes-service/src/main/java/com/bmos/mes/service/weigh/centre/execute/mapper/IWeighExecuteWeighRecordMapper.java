package com.bmos.mes.service.weigh.centre.execute.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.ingredient.WeighType;
import com.bmos.mes.service.weigh.centre.execute.model.WeighExecuteWeighRecord;
import com.bmos.mes.service.weigh.centre.execute.vo.WeighExecuteWeighRecordResult;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/7/10 19:03
 */
@Mapper
public interface IWeighExecuteWeighRecordMapper extends BaseMapperX<WeighExecuteWeighRecord> {

    /**
     * 根据需求id查询称量记录
     * @param requirementId 需求id
     * @return 称量记录
     */
    default List<WeighExecuteWeighRecord> selectListByRequirementId(Long requirementId){
        if (requirementId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteWeighRecord>()
                .eq(WeighExecuteWeighRecord::getRequirementId, requirementId)
                .orderByAsc(WeighExecuteWeighRecord::getWeighTime)
        );
    }

    default List<WeighExecuteWeighRecord> selectListByRequirementIds(Collection<Long> requirementIds){
        if (CollectionUtils.isAnyEmpty(requirementIds)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteWeighRecord>()
                .in(WeighExecuteWeighRecord::getRequirementId, requirementIds)
        );
    }

    /**
     * 根据需求id和物料批次id查询称量记录
     * @param taskId 任务id
     * @param weighType 称量类型
     * @return
     */
    default List<WeighExecuteWeighRecord> queryListByTaskIdAndType(Long taskId, WeighType weighType){
        if (taskId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteWeighRecord>()
                .eq(WeighExecuteWeighRecord::getTaskId, taskId)
                .eq(weighType != null, WeighExecuteWeighRecord::getWeighType, weighType)
        );
    }

    default List<WeighExecuteWeighRecord> queryListByTaskId(Long taskId){
        if (taskId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteWeighRecord>()
                .eq(WeighExecuteWeighRecord::getTaskId, taskId)
        );
    }

    /**
     * 根据任务id查询称量记录列表
     * @param taskId 任务id
     * @return
     */
    List<WeighExecuteWeighRecordResult> queryRecordResultByTaskId(@Param("taskId") Long taskId);

    /**
     * 根据物料id查询
     * @param id
     * @return
     */
    default WeighExecuteWeighRecord queryByStorageMaterialId(Long id){
        return selectOne(new LambdaQueryWrapper<WeighExecuteWeighRecord>()
                .eq(WeighExecuteWeighRecord::getStorageMaterialId, id)
        );
    }

    default List<WeighExecuteWeighRecord> queryListByTaskIdAndBatchId(Long id, Long storageMaterialBatchId){
        if (id == null || storageMaterialBatchId == null){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighExecuteWeighRecord>()
                .eq(WeighExecuteWeighRecord::getTaskId, id)
                .eq(WeighExecuteWeighRecord::getStorageMaterialBatchId, storageMaterialBatchId)
        );
    }
}
