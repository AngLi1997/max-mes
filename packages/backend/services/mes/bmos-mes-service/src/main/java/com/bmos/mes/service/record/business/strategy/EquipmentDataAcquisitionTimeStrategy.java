package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.model.execute.ExecuteFormDataBaseExtInfo;
import com.bmos.mes.common.utils.TimeUtil;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.dto.BusinessComponentBatchSaveDTO;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数采组件-数采时间策略
 *
 * @author yigaohui
 * @date 2024/4/24
 **/
@Service("EQUIPMENT_DATA_ACQUISITION_TIME")
@Slf4j
public class EquipmentDataAcquisitionTimeStrategy implements BusinessComponentStrategy {
    private static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String CONFIG_JSON_FIELD = "dateFormat";
    private static final Map<String, String> FORMAT_MAPPING = new HashMap<String, String>(){{
        put("yyyy-MM-dd HH:mm:ss", "yMdHms");
        put("yyyy-MM-dd HH:mm", "yMdHm");
        put("yyyy-MM-dd", "yMd");
        put("yyyy-MM", "yM");
        put("MM-dd HH:mm:ss", "MdHms");
        put("HH:mm:ss", "Hms");
        put("HH:mm", "Hm");
    }};

    @Resource
    private PlatformParameterClientImpl parameterClient;

    @Override
    public void handleBusinessComponent(@NotNull List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info,
                                        Map<Long, BusinessComponentConfigDetailVO> configMap, Integer index) {
        BusinessComponentBatchSaveDTO dto = info.getDto();
        ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(dto);
        convert.setFieldId(component.getFieldId());
        convert.setComponentType(component.getComponentType());
        LocalDateTime now = info.getAcquisitionTime() == null ? LocalDateTime.now() : info.getAcquisitionTime();
        convert.setValue(now.format(DateTimeFormatter.ofPattern(getPlatformParamPattern(configMap.get(component.getId())))));
        convert.setExtInfo(JsonUtils.toJsonString(new ExecuteFormDataBaseExtInfo(String.valueOf(TimeUtil.getTimestamp(now)))));
        results.add(convert);
    }

    private String getPlatformParamPattern(BusinessComponentConfigDetailVO detail) {
        String formatPattern = getFormatPattern(detail);
        String valueByCode = parameterClient.getValueByCode(BusinessParameterCodeConstants.PLATFORM_SYS_TIME_FORMAT);
        JSONObject patternObj = JSONUtil.parseObj(valueByCode);
        String pattern = patternObj.get(FORMAT_MAPPING.get(formatPattern), String.class);
        if (StrUtil.isEmpty(pattern)) {
            return formatPattern;
        }
        return pattern;
    }

    private String getFormatPattern(BusinessComponentConfigDetailVO businessComponentConfigDetailVO) {
        if (businessComponentConfigDetailVO == null || StringUtils.isEmpty(businessComponentConfigDetailVO.getConfigInfo())) {
            log.info("没有时间格式配置，返回默认格式{}", DATETIME_FORMAT_PATTERN);
            return DATETIME_FORMAT_PATTERN;
        }
        String configInfo = businessComponentConfigDetailVO.getConfigInfo();
        JSONObject jsonObject = JSONUtil.parseObj(configInfo);
        String formatStr = jsonObject.getStr(CONFIG_JSON_FIELD);
        if (StringUtils.isEmpty(formatStr)) {
            log.warn("没有找到对应的时间格式配置，返回默认格式{}", DATETIME_FORMAT_PATTERN);
            return DATETIME_FORMAT_PATTERN;
        }
        return formatStr;
    }
}
