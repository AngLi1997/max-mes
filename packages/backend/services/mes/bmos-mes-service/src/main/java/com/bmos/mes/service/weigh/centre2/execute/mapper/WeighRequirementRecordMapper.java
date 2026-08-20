package com.bmos.mes.service.weigh.centre2.execute.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.weigh.centre2.SignStatusEnum;
import com.bmos.mes.common.enums.weigh.centre2.WeighTypeEnum;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighRequirementQualityDO;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighRequirementRecordDO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mapper
public interface WeighRequirementRecordMapper extends BaseMapperX<WeighRequirementRecordDO> {
    // 查询某需求下所有正常称量记录
    default List<WeighRequirementRecordDO> listByRequirementId(Long requirementId) {
        return selectList(new LambdaQueryWrapper<WeighRequirementRecordDO>()
                .eq(WeighRequirementRecordDO::getWeighTicketRequirementId, requirementId)
                .eq(WeighRequirementRecordDO::getWeighType, WeighTypeEnum.NORMAL)
        );
    }
    // 查询某需求下所有余料称量记录
    default List<WeighRequirementRecordDO> listOddmentByTicketId(Long ticketId) {
        return selectList(new LambdaQueryWrapper<WeighRequirementRecordDO>()
                .eq(WeighRequirementRecordDO::getTicketId, ticketId)
                .eq(WeighRequirementRecordDO::getWeighType, WeighTypeEnum.ODDMENT)
        );
    }
    // 插入称量记录
    default void insertRecord(WeighRequirementRecordDO record) {
        insert(record);
    }

    default boolean existsNotSign(Long ticketId){
        return exists(new LambdaQueryWrapper<WeighRequirementRecordDO>()
                .eq(WeighRequirementRecordDO::getTicketId, ticketId)
                .eq(WeighRequirementRecordDO::getSignStatus, SignStatusEnum.UNSIGNED));
    }

    default List<WeighRequirementRecordDO> listByTicketIdAndWeighType(Long ticketId, WeighTypeEnum weighTypeEnum){
        if (weighTypeEnum == null) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighRequirementRecordDO>()
                .eq(WeighRequirementRecordDO::getTicketId, ticketId)
                .eq(WeighRequirementRecordDO::getWeighType, weighTypeEnum));
    }

    default List<WeighRequirementRecordDO> listByTicketId(Long ticketId){
        return selectList(new LambdaQueryWrapper<WeighRequirementRecordDO>()
                .eq(WeighRequirementRecordDO::getTicketId, ticketId));
    }

    default List<WeighRequirementRecordDO> selectByRequirementIds(List<Long> ids){
        if (CollectionUtils.isAnyEmpty(ids)){
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapper<WeighRequirementRecordDO>()
                .in(WeighRequirementRecordDO::getWeighTicketRequirementId, ids));
    }

    default WeighRequirementRecordDO queryByStorageMaterialId(Long id){
        if (id == null){
            return null;
        }
        return selectOne(new LambdaQueryWrapper<WeighRequirementRecordDO>()
                .eq(WeighRequirementRecordDO::getStorageMaterialId, id));
    }

    default List<WeighRequirementRecordDO> selectByTicketIdNotSign(Long ticketId){
        return selectList(new LambdaQueryWrapper<WeighRequirementRecordDO>()
                .eq(WeighRequirementRecordDO::getTicketId, ticketId)
                .eq(WeighRequirementRecordDO::getSignStatus, SignStatusEnum.UNSIGNED));
    }

}