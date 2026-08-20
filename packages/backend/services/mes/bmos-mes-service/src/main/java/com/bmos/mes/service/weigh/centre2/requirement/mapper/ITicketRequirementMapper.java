package com.bmos.mes.service.weigh.centre2.requirement.mapper;

import com.bmos.audit.engine.core.db.repository.base.BaseMapperX;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.mes.common.enums.weigh.centre.RequirementStatusEnum;
import com.bmos.mes.service.weigh.centre2.requirement.dto.RequirementQueryDTO;
import com.bmos.mes.service.weigh.centre2.requirement.entity.TicketRequirementDO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementOccupancyQuantityResult;
import com.bmos.mes.service.weigh.centre2.requirement.vo.TicketRequirementVO;
import com.bmos.mes.service.weigh.centre2.requirement.vo.WeighRequirementListVO;
import com.bmos.mybatis.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 配料信息物料查询Mapper接口
 *
 * @author liang
 * @version 1.0.0
 * @date 2025/5/19 19:16
 */
@Mapper
public interface ITicketRequirementMapper extends BaseMapperX<TicketRequirementDO> {

    /**
     * 根据物料ID查询配料信息物料列表
     *
     * @param materialId 物料ID
     * @return 配料信息物料列表
     */
    List<TicketRequirementVO> selectMaterialList(@Param("materialId") Long materialId);

    /**
     * 查询占有量数据
     *
     * @param storageMaterialBatchIds 物料批次id列表
     * @return 每个批次的占有量
     */
    List<TicketRequirementOccupancyQuantityResult> selectOccupancyQuantity(@Param("storageMaterialBatchIds") List<Long> storageMaterialBatchIds, @Param("expectKey") String expectKey);

    default List<TicketRequirementDO> selectByRequirementGroupId(Long groupId) {
        return selectList(new LambdaQueryWrapperX<TicketRequirementDO>()
                .eq(TicketRequirementDO::getRequirementGroupId, groupId)
        );
    }

    default List<TicketRequirementDO> selectByRequirementGroupIds(List<Long> requirementGroupIds) {
        if (CollectionUtils.isAnyEmpty(requirementGroupIds)) {
            return new ArrayList<>();
        }
        return selectList(new LambdaQueryWrapperX<TicketRequirementDO>()
                .in(TicketRequirementDO::getRequirementGroupId, requirementGroupIds)
        );
    }

    /**
     * 根据工单ID查询所有需求
     */
    default List<TicketRequirementDO> getRequirementsByTicketId(@Param("ticketId") Long ticketId) {
        return selectList(new LambdaQueryWrapperX<TicketRequirementDO>()
                .eq(TicketRequirementDO::getTicketId, ticketId));
    }

    /**
     * 查询工单下处于WEIGHING状态的需求
     */
    default List<TicketRequirementDO> selectWeighingByTicketId(@Param("ticketId") Long ticketId) {
        return selectList(new LambdaQueryWrapperX<TicketRequirementDO>()
                .eq(TicketRequirementDO::getTicketId, ticketId)
                .eq(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.WEIGHING));
    }

    /**
     * 查询工单下处于UN_WEIGHED或WEIGHING状态的需求
     */
    default List<TicketRequirementDO> listUnweighedOrWeighingByTicketId(@Param("ticketId") Long ticketId) {
        return selectList(new LambdaQueryWrapperX<TicketRequirementDO>()
                .eq(TicketRequirementDO::getTicketId, ticketId)
                .in(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.UN_WEIGHED, RequirementStatusEnum.WEIGHING));
    }

    default List<Long> listAutoProgramRequirements() {
        return selectList(new LambdaQueryWrapperX<TicketRequirementDO>()
                .eq(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.UN_PLANNED))
                .stream()
                .map(TicketRequirementDO::getId)
                .collect(Collectors.toList());
    }

    List<WeighRequirementListVO> queryRequirementList(@Param("query") RequirementQueryDTO queryDTO, @Param("centreIds") Collection<Long> centreIds);

    default List<TicketRequirementDO> listByTicketId(Long ticketId){
        return selectList(new LambdaQueryWrapperX<TicketRequirementDO>()
                .eq(TicketRequirementDO::getTicketId, ticketId)
                .in(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.UN_WEIGHED, RequirementStatusEnum.WEIGHING));
    }

    /**
     * 查询工单下最后一个完成状态的需求
     * @param ticketId
     * @return
     */
    default TicketRequirementDO selectLastCompleteRequirementByTicketId(Long ticketId){
        return selectOne(new LambdaQueryWrapperX<TicketRequirementDO>()
                .eq(TicketRequirementDO::getTicketId, ticketId)
                .eq(TicketRequirementDO::getRequirementStatus, RequirementStatusEnum.WEIGHED)
                .orderByDesc(TicketRequirementDO::getCompleteTime)
                .last("limit 1"));
    }
}