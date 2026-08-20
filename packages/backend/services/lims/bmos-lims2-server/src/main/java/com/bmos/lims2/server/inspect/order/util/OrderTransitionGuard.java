package com.bmos.lims2.server.inspect.order.util;

import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.common.i18n.LimsResponseCode;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * 检验单状态流转校验器
 * @Author: yigaohui
 * @Date: 2025/09/19 11:10
 */
public final class OrderTransitionGuard {

    private static final Map<InspectionOrderStatusEnum, Set<InspectionOrderStatusEnum>> ALLOWED = new EnumMap<>(InspectionOrderStatusEnum.class);

    static {
        // 未确认 → 已确认
        ALLOWED.put(InspectionOrderStatusEnum.PENDING_CONFIRM, EnumSet.of(InspectionOrderStatusEnum.CONFIRMED, InspectionOrderStatusEnum.TERMINATED));
        // 已确认 → 样品待审核（当所有任务复核通过时）
        ALLOWED.put(InspectionOrderStatusEnum.CONFIRMED, EnumSet.of(InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING, InspectionOrderStatusEnum.TERMINATED));
        // 样品待审核 → 完成 / 审核未通过
        ALLOWED.put(InspectionOrderStatusEnum.SAMPLE_AUDIT_PENDING, EnumSet.of(InspectionOrderStatusEnum.COMPLETED, InspectionOrderStatusEnum.SAMPLE_AUDIT_REJECTED, InspectionOrderStatusEnum.TERMINATED));
        // 审核未通过 → 回落为已确认（允许再次修改并重新提交）、或终止
        ALLOWED.put(InspectionOrderStatusEnum.SAMPLE_AUDIT_REJECTED, EnumSet.of(InspectionOrderStatusEnum.CONFIRMED, InspectionOrderStatusEnum.TERMINATED));
        // 终止/完成为终态
        ALLOWED.put(InspectionOrderStatusEnum.TERMINATED, Collections.emptySet());
        ALLOWED.put(InspectionOrderStatusEnum.COMPLETED, Collections.emptySet());
    }

    private OrderTransitionGuard() {}

    public static boolean isAllowed(InspectionOrderStatusEnum from, InspectionOrderStatusEnum to) {
        if (from == null || to == null) return false;
        if (to == InspectionOrderStatusEnum.TERMINATED) return true;
        Set<InspectionOrderStatusEnum> next = ALLOWED.get(from);
        return next != null && next.contains(to);
    }

    public static void checkOrThrow(InspectionOrderStatusEnum from, InspectionOrderStatusEnum to) {
        if (!isAllowed(from, to)) {
            throw new BmosException(LimsResponseCode.ORDER_STATUS_CHANGE_ERROR, from.getName()  ,to.getName());
        }
    }
}
