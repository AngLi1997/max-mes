package com.bmos.wms.service.inspect.lims;

import com.alibaba.fastjson.JSONObject;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import com.bmos.wms.service.platform.parameter.impl.PlatformParameterClientImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 读取并解析 {@link BusinessParameterCodeConstants#INSPECT_LIMS_CONFIG} 平台参数。
 *
 * <p>与 mes 同款；任何解析异常按"不对接"兜底，避免 LIMS 故障扩散到 WMS 主流程。
 */
@Slf4j
@Component
public class InspectLimsSwitch {

    @Autowired
    private PlatformParameterClientImpl platformParameterClient;

    @Getter
    public static class Config {
        private final boolean enabled;
        private final LimsType type;

        public Config(boolean enabled, LimsType type) {
            this.enabled = enabled;
            this.type = type;
        }
    }

    /** 读取并解析 inspect.lims.config；解析失败按"不对接"兜底。 */
    public Config current() {
        String raw = platformParameterClient.getValueByCode(BusinessParameterCodeConstants.INSPECT_LIMS_CONFIG);
        return parse(raw);
    }

    static Config parse(String raw) {
        if (StringUtils.isBlank(raw)) {
            return new Config(false, LimsType.THIRD_PARTY);
        }
        try {
            JSONObject json = JSONObject.parseObject(raw);
            boolean enabled = json.getBooleanValue("enabled");
            LimsType type = LimsType.of(json.getString("type"));
            return new Config(enabled, type);
        } catch (Exception e) {
            log.error("解析 inspect.lims.config 失败，按不对接处理。raw={}", raw, e);
            return new Config(false, LimsType.THIRD_PARTY);
        }
    }
}
