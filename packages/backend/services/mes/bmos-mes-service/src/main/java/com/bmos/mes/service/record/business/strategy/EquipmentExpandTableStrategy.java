package com.bmos.mes.service.record.business.strategy;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.bmos.common.util.collection.CollectionUtils;
import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.service.equipment.mapper.entity.ProcedureEquipmentAcquisition;
import com.bmos.mes.service.execute.convert.ExecuteFormDataConverter;
import com.bmos.mes.service.execute.model.ExecuteFormData;
import com.bmos.mes.service.execute.vo.BusinessComponentConfigDetailVO;
import com.bmos.mes.service.platform.parameter.impl.PlatformParameterClientImpl;
import com.bmos.mes.service.record.business.BusinessComponentStrategy;
import com.bmos.mes.service.record.business.model.ExpandTableInfo;
import com.bmos.mes.service.record.business.model.ProductionDetailInfo;
import com.bmos.mes.service.record.vo.ComponentListVO;
import com.bmos.mes.service.utils.UserUtils;
import com.bmos.platform.facade.system.execute.parameter.constants.BusinessParameterCodeConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service("EQUIPMENT_DATA_ACQUISITION_DYNAMIC_TABLE")
@Slf4j
public class EquipmentExpandTableStrategy implements BusinessComponentStrategy {


    private final String EQUIPMENT_CODE = "equipmentCode";

    private final String EQUIPMENT_NAME = "equipmentName";

    private final String ACQUISITION_TIME = "acquisitionTime";

    private final String ACQUISITION_USER = "acquisitionUser";

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
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component,
                                        ProductionDetailInfo info, Map<Long, BusinessComponentConfigDetailVO> configMap,
                                        Integer index) {
        String componentDetail = component.getComponentDetail();
        ExpandTableInfo expandTableInfo = JsonUtils.parseObject(componentDetail, ExpandTableInfo.class);
        if (expandTableInfo == null || CollUtil.isEmpty(expandTableInfo.getTableList())) {
            return;
        }
        List<ProcedureEquipmentAcquisition> equipmentAcquisitionList = info.getEquipmentAcquisitionList();
        if (CollUtil.isEmpty(equipmentAcquisitionList)) {
            return;
        }
        // 采集数据分批
        Collection<List<ProcedureEquipmentAcquisition>> acquisitionList = equipmentAcquisitionList.stream()
                .collect(Collectors.groupingBy(
                        ProcedureEquipmentAcquisition::getAcquisitionSort,
                        LinkedHashMap::new,
                        Collectors.toList()
                )).values();
        // 获取时间格配置
        String formatPattern = getPlatformParamPattern(configMap.get(component.getId()));
        // 采集数据对应成map数组 保证实施配置修改后也能展示
        List<Map<String, String>> values = acquisitionList.stream()
                .map(e->{
                    ProcedureEquipmentAcquisition first = CollUtil.getFirst(e);
                    // dataDictCode 对应配置中的colData
                    Map<String, String> codeMap = CollectionUtils.convertMap(e, ProcedureEquipmentAcquisition::getDataDictCode, ProcedureEquipmentAcquisition::getDataPointValue);
                    handleCodeMap(first, codeMap);
                    Map<String, String> map = new HashMap<>();
                    expandTableInfo.getTableList().forEach(c -> {
                        String columnDataCode = c.getColData();
                        map.put(columnDataCode, getValue(codeMap, columnDataCode, formatPattern));
                    });
                    return map;
                })
                .collect(Collectors.toList());
        ExecuteFormData convert = ExecuteFormDataConverter.INSTANCE.convert(info.getDto());
        convert.setFieldId(component.getFieldId());
        convert.setComponentType(component.getComponentType());
        convert.setValue(JsonUtils.toJsonString(values));
        results.add(convert);
    }

    private String getValue(Map<String, String> codeMap, String columnDataCode, String formatPattern) {
        String value = codeMap.get(columnDataCode);
        if (ACQUISITION_TIME.equals(columnDataCode)) {
            return LocalDateTimeUtil.format(LocalDateTimeUtil.parse(value, DATETIME_FORMAT_PATTERN), formatPattern);
        }
        return  value;
    }

    /**
     * 处理数采基础信息
     * @param first
     * @param codeMap
     */
    private void handleCodeMap(ProcedureEquipmentAcquisition first, Map<String, String> codeMap) {
        codeMap.put(EQUIPMENT_CODE, first.getEquipmentCode());
        codeMap.put(EQUIPMENT_NAME, first.getEquipmentName());
        codeMap.put(ACQUISITION_USER, UserUtils.getUsername(first.getCreateBy()));
        codeMap.put(ACQUISITION_TIME, LocalDateTimeUtil.formatNormal(first.getAcquisitionTime()));
    }

    public String getPlatformParamPattern(BusinessComponentConfigDetailVO detail) {
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
