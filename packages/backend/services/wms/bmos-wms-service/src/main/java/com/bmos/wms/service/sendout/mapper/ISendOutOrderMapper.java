package com.bmos.wms.service.sendout.mapper;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.bmos.mybatis.mapper.BaseMapperX;
import com.bmos.wms.common.enums.sendout.SendOrderStatus;
import com.bmos.wms.service.sendout.dto.SendPageQuery;
import com.bmos.wms.service.sendout.model.SendOutOrder;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/4/15 09:33
 */
@Mapper
public interface ISendOutOrderMapper extends BaseMapperX<SendOutOrder> {

    /**
     * 查询领料计划id是否寻在
     *
     * @param requisitionPlanId 领料计划id
     * @return
     */
    default boolean existRequisitionPlanId(Long requisitionPlanId) {
        if (requisitionPlanId == null) {
            return false;
        }
        return exists(Wrappers.lambdaQuery(SendOutOrder.class).eq(SendOutOrder::getRequisitionPlanId, requisitionPlanId));
    }

    /**
     * 查询待发料领料工单分页
     *
     * @param pageQuery 分页查询参数
     * @return
     */
    default List<SendOutOrder> queryPendingPage(SendPageQuery pageQuery) {
        return selectList(Wrappers.lambdaQuery(SendOutOrder.class)
                .like(StrUtil.isNotEmpty(pageQuery.getPullOrderNo()), SendOutOrder::getPullOrderNo, pageQuery.getPullOrderNo())
                .like(StrUtil.isNotEmpty(pageQuery.getProductName()), SendOutOrder::getProductName, pageQuery.getProductName())
                .like(StrUtil.isNotEmpty(pageQuery.getBatchNo()), SendOutOrder::getBatchNo, pageQuery.getBatchNo())
                .eq(SendOutOrder::getSendOrderStatus, SendOrderStatus.PENDING.getValue())
                .orderByDesc(SendOutOrder::getSubmitTime)
        );
    }
}
