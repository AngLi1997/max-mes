package com.bmos.mes.service.weigh.centre2.execute.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.mes.service.weigh.centre2.execute.model.WeighStorageMaterialRequirementDO;
import com.bmos.mybatis.mapper.BaseMapperX;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface WeighStorageMaterialRequirementMapper extends BaseMapperX<WeighStorageMaterialRequirementDO> {
    // 查询某需求下所有物料件绑定
    default List<WeighStorageMaterialRequirementDO> listByRequirementId(Long requirementId) {
        return selectList(new LambdaQueryWrapper<WeighStorageMaterialRequirementDO>()
                .eq(WeighStorageMaterialRequirementDO::getWeighTicketRequirementId, requirementId)
        );
    }
    // 插入物料件绑定，支持consumeQuantity
    default void insertBind(Long requirementId, Long storageMaterialId, java.math.BigDecimal consumeQuantity) {
        WeighStorageMaterialRequirementDO bind = new WeighStorageMaterialRequirementDO();
        bind.setWeighTicketRequirementId(requirementId);
        bind.setStorageMaterialId(storageMaterialId);
        bind.setConsumeQuantity(consumeQuantity);
        insert(bind);
    }
    // 批量插入物料件绑定
    default void insertBatchBind(Long requirementId, List<WeighStorageMaterialRequirementDO> binds) {
        for (WeighStorageMaterialRequirementDO bind : binds) {
            bind.setWeighTicketRequirementId(requirementId);
            insert(bind);
        }
    }
} 