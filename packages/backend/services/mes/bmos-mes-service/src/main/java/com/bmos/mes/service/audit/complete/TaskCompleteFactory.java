package com.bmos.mes.service.audit.complete;

import com.bmos.mes.common.enums.audit.FlowAuditCodeEnum;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TaskCompleteFactory {

    private final Map<String, TaskComplete> completeCache = new ConcurrentHashMap<>();

    public TaskComplete getRule(String completeType) {
        return getCompleteByType(completeType);
    }

    protected TaskComplete getCompleteByType(String completeType) {
        if (FlowAuditCodeEnum.COUNTERSIGN.getValue().equals(completeType)) {
            return completeCache.computeIfAbsent(completeType, key -> new CountersignComplete());
        }
        if (FlowAuditCodeEnum.OR_VISE.getValue().equals(completeType)) {
            return completeCache.computeIfAbsent(completeType, key -> new OrViseComplete());
        }
        return null;
    }
}
