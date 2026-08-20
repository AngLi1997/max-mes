package com.bmos.wms.service.inspect.lims;

import com.bmos.common.exception.BmosException;
import com.bmos.common.response.ResponseItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 按平台参数 inspect.lims.config 解析当前生效的 LIMS 网关。
 *
 * <p>WMS 当前只支持 BMOS 类型；遇到 THIRD_PARTY 显式拒绝（spec §1.2）。
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

    /** 当前是否走自研（BMOS）路径。 */
    public boolean isBmos() {
        InspectLimsSwitch.Config cfg = inspectLimsSwitch.current();
        return cfg.isEnabled() && cfg.getType() == LimsType.BMOS;
    }

    /**
     * 取当前生效网关；未启用或非 BMOS 类型抛业务异常。
     */
    public LimsInspectGateway require() {
        InspectLimsSwitch.Config cfg = inspectLimsSwitch.current();
        if (!cfg.isEnabled()) {
            throw new BmosException(ResponseItem.from(83_11_001, "LIMS 对接未启用，无法发起检验", "bmosWms"));
        }
        if (cfg.getType() != LimsType.BMOS) {
            throw new BmosException(ResponseItem.from(83_11_002, "WMS 暂不支持三方 LIMS，仅支持自研 LIMS", "bmosWms"));
        }
        LimsInspectGateway g = registry.get(LimsType.BMOS);
        if (g == null) {
            throw new BmosException(ResponseItem.from(83_11_003, "BMOS LIMS 网关未就绪", "bmosWms"));
        }
        return g;
    }
}
