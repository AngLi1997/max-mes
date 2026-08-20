package com.bmos.mes.service.inspect.lims;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 按当前平台参数开关解析应使用的 LIMS 网关。
 */
@Component
public class LimsGatewaySelector {

    private final Map<LimsType, LimsInspectGateway> registry = new EnumMap<>(LimsType.class);

    @Autowired
    private InspectLimsSwitch inspectLimsSwitch;

    @Autowired
    public LimsGatewaySelector(List<LimsInspectGateway> gateways) {
        for (LimsInspectGateway g : gateways) {
            registry.put(g.type(), g);
        }
    }

    /** 当前是否对接 LIMS。 */
    public boolean enabled() {
        return inspectLimsSwitch.current().isEnabled();
    }

    /** 当前生效网关（未启用时返回 null）。 */
    public LimsInspectGateway current() {
        InspectLimsSwitch.Config cfg = inspectLimsSwitch.current();
        if (!cfg.isEnabled()) {
            return null;
        }
        return registry.get(cfg.getType());
    }

    /** 当前是否自研路径。 */
    public boolean isBmos() {
        InspectLimsSwitch.Config cfg = inspectLimsSwitch.current();
        return cfg.isEnabled() && cfg.getType() == LimsType.BMOS;
    }
}
