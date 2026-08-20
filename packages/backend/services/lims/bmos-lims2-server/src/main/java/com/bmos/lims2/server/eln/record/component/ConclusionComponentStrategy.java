package com.bmos.lims2.server.eln.record.component;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.exception.BmosException;
import com.bmos.lims2.common.i18n.LimsResponseCode;
import com.bmos.lims2.server.eln.entry.converter.ExecuteFormDataConverter;
import com.bmos.lims2.server.eln.entry.dto.ElnEntryContext;
import com.bmos.lims2.server.eln.entry.entity.ExecuteFormData;
import com.bmos.lims2.server.eln.record.vo.ComponentListVO;
import com.bmos.common.util.json.JsonUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @Description: 结论组件策略（依据布尔值选择配置项生成表单值）
 * @Author: yigaohui
 * @Date: 2025/11/19 10:20
 */
@Service("CONCLUSION")
public class ConclusionComponentStrategy implements BusinessComponentStrategy {

    private static final String ERR_CONTEXT_MISSING = "结论组件入参缺失上下文信息";
    private static final String ERR_COMPONENT_CONFIG_INVALID = "结论组件配置异常（缺少fieldId）";
    private static final String ERR_RESULT_NULL = "结论布尔结果为空";
    private static final String ERR_MAPPING_EMPTY = "结论组件未配置选项映射";
    @Override
    public void handleBusinessComponent(List<ExecuteFormData> results, ComponentListVO component, ElnEntryContext context) {
        if (ObjectUtil.isNull(context) || ObjectUtil.isNull(context.getDto())) {
            throw new BmosException(LimsResponseCode.CONCLUSION_CONTEXT_MISSING, ERR_CONTEXT_MISSING);
        }
        if (ObjectUtil.isNull(component) || component.getFieldId() == null) {
            throw new BmosException(LimsResponseCode.CONCLUSION_COMPONENT_INVALID, ERR_COMPONENT_CONFIG_INVALID);
        }
        if (context.getConclusionResult() == null) {
            throw new BmosException(LimsResponseCode.CONCLUSION_RESULT_NULL, ERR_RESULT_NULL);
        }
        String detail = component.getComponentDetail();
        List<OptionMapping> mappings = JsonUtils.parseArray(detail, OptionMapping.class);
        if (CollUtil.isEmpty(mappings)) {
            throw new BmosException(LimsResponseCode.CONCLUSION_MAPPING_EMPTY, ERR_MAPPING_EMPTY);
        }
        String expect = context.getConclusionResult() ? "true" : "false";
        OptionMapping target = mappings.stream()
                .filter(e -> StrUtil.equalsIgnoreCase(e.getValue(), expect))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(target)) {
            throw new BmosException(LimsResponseCode.CONCLUSION_MAPPING_NOT_FOUND, expect);
        }
        ExecuteFormData form = ExecuteFormDataConverter.INSTANCE.convert(context.getDto());
        form.setFieldId(component.getFieldId());
        form.setComponentType(component.getComponentType());
        // 将配置中的 field 写入值，value 写入扩展
        form.setValue(target.getValue());
        form.setValueExtension(target.getField());
        form.setOperationTime(LocalDateTime.now());
        results.add(form);
    }

    public static class OptionMapping {
        private String field;
        private String value;

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

