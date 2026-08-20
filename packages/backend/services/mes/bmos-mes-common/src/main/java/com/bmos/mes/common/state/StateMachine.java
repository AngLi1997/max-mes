package com.bmos.mes.common.state;

import cn.hutool.core.lang.Assert;
import com.bmos.common.exception.BmosException;
import com.bmos.mes.common.exception.MesResponseCode;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 状态机
 */
public class StateMachine<S, E> {
    private final Map<SourceStatusEvent<S, E>, S> STATE_FLOWS = new HashMap<>();

    public void accept(S sourceStatus, E event, S targetStatus) {
        SourceStatusEvent<S, E> sourceStatusEvent = new SourceStatusEvent<>(sourceStatus, event);
        STATE_FLOWS.put(sourceStatusEvent, targetStatus);
    }

    public S getNextStatus(S sourceStatus, E event) {
        S nextStatus = STATE_FLOWS.get(new SourceStatusEvent<>(sourceStatus, event));
        Assert.notNull(nextStatus, () -> new BmosException(MesResponseCode.STATUS_ERROR));
        return nextStatus;
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    static class SourceStatusEvent<S, E> {
        private final S sourceStatus;
        private final E event;
    }
}
