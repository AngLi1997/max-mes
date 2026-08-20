package com.bmos.mes.service.execute.model.calculate;

import com.bmos.common.util.json.JsonUtils;
import com.bmos.mes.common.enums.record.BasicComponentTypeEnum;
import com.bmos.mes.service.record.model.BatchRecordComponent;
import lombok.Getter;

import java.util.Collections;
import java.util.Objects;

@Getter
public class CalculateResult {
    /**
     * 计算值
     */
    private String value;

    /**
     * 拓展值
     * 日期时间相关的计算需要返回拓展值
     */
    private String extInfo;

    /**
     * 计算错误
     */
    private boolean error;


    /**
     * 组件
     */
    private BatchRecordComponent component;

    /**
     * 是否是空值
     */
    private Boolean emptyValue;

    public CalculateResult() {
    }

    public CalculateResult(BatchRecordComponent component, String value) {
        this.value = value;
        this.component = component;
    }

    public CalculateResult(BatchRecordComponent component, String value, String extInfo) {
        this.value = value;
        this.extInfo = extInfo;
        this.component = component;
    }

    public void calculateError(String errorValue) {
        this.error = true;
        if (component != null && Objects.equals(component.getComponentType(),
                BasicComponentTypeEnum.CHECKBOX.getValue())) {
            this.value = JsonUtils.toJsonString(Collections.singletonList(errorValue));
        } else {
            this.value = errorValue;
        }
    }

    public CalculateResult emptyValue(Boolean empty) {
        this.emptyValue = empty;
        return this;
    }

    public CalculateResult emptyValue(String emptyParam) {
        this.emptyValue = true;
        this.value = emptyParam;
        return this;
    }

}