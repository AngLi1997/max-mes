package com.bmos.lims2.server.audit.message.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.server.audit.vo.AuditMessageVO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 样品审核消息策略
 * @Author: yigaohui
 * @Date: 2025/09/03 16:40
 */
@Service
public class SampleAuditStrategy extends AbstractAuditMessageStrategy {

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    public SampleAuditStrategy(PlatformApiAdaptor platformApiAdaptor) {
        super(platformApiAdaptor);
    }

    @Override
    List<String> getAuditBusinessKey(List<Long> deptIdList) {
        // 查询处于样品审核中的检验单，返回其业务键（订单ID）
        List<InspectionOrder> orders = inspectionOrderMapper.selectList(
                new LambdaQueryWrapper<InspectionOrder>()
                        .eq(InspectionOrder::getOrderStatus, InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING)
                        .isNotNull(InspectionOrder::getSampleAuditProcessInstanceId)
        );
        return orders.stream()
                .map(InspectionOrder::getId)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    AuditMessageVO getPermissionHandle(Long businessId) {
        // 基于订单ID进行数据权限定位
        InspectionOrder order = inspectionOrderMapper.selectById(businessId);
        if (order == null) {
            return null;
        }
        AuditMessageVO vo = new AuditMessageVO();
        vo.setBusinessId(order.getId());
        vo.setUserIdList(new ArrayList<>());
        return vo;
    }
}


