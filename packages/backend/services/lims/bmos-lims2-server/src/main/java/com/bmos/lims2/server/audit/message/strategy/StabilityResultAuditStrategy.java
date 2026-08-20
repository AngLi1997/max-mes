package com.bmos.lims2.server.audit.message.strategy;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.bmos.adaptor.platform.PlatformApiAdaptor;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.server.audit.vo.AuditMessageVO;
import com.bmos.lims2.server.inspect.order.entity.InspectionOrder;
import com.bmos.lims2.server.inspect.order.mapper.InspectionOrderMapper;
import com.bmos.lims2.server.stability.plan.mapper.StabilityPlanTimepointTaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 稳定性结果审核消息策略
 */
@Service
public class StabilityResultAuditStrategy extends AbstractAuditMessageStrategy {

    @Autowired
    private InspectionOrderMapper inspectionOrderMapper;

    @Autowired
    private StabilityPlanTimepointTaskMapper timepointTaskMapper;

    public StabilityResultAuditStrategy(PlatformApiAdaptor platformApiAdaptor) {
        super(platformApiAdaptor);
    }

    @Override
    List<String> getAuditBusinessKey(List<Long> deptIdList) {
        // 查询与稳定性时间点任务关联的待审核检验单
        Set<Long> stabilityOrderIds = timepointTaskMapper.selectList(
                new LambdaQueryWrapper<com.bmos.lims2.server.stability.plan.entity.StabilityPlanTimepointTask>()
                        .isNotNull(com.bmos.lims2.server.stability.plan.entity.StabilityPlanTimepointTask::getInspectionOrderId)
        ).stream()
                .map(com.bmos.lims2.server.stability.plan.entity.StabilityPlanTimepointTask::getInspectionOrderId)
                .collect(Collectors.toSet());

        if (stabilityOrderIds.isEmpty()) {
            return new ArrayList<>();
        }

        List<InspectionOrder> orders = inspectionOrderMapper.selectList(
                new LambdaQueryWrapper<InspectionOrder>()
                        .eq(InspectionOrder::getOrderStatus, InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING)
                        .isNotNull(InspectionOrder::getSampleAuditProcessInstanceId)
                        .in(InspectionOrder::getId, stabilityOrderIds)
        );
        return orders.stream()
                .map(InspectionOrder::getId)
                .distinct()
                .map(String::valueOf)
                .collect(Collectors.toList());
    }

    @Override
    AuditMessageVO getPermissionHandle(Long businessId) {
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
