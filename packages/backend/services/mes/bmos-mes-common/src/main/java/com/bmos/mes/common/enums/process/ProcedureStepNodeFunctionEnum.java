package com.bmos.mes.common.enums.process;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 工序步骤节点功能
 */
@Getter
@AllArgsConstructor
public enum ProcedureStepNodeFunctionEnum implements CommonEnum<String> {

    RECORD_WORK("记录作业", "0"),
    PROCEDURE_AUDIT("工序审核", "1"),
    PROCESS_AUDIT("工艺审核", "2"),
    PROCEDURE_CHANGE_TEAM("工序换班", "3"),
    PROCESS_CHANGE_TEAM("工艺换班", "4"),
    SUB_RECORD("辅助记录","5"),
    INSPECT("发起请验","6")
    ;

    private final String name;
    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    /**
     * 根据value查询记录作业枚举
     * 无匹配值则返回 记录作业
     * @param value
     * @return
     */
    public static ProcedureStepNodeFunctionEnum getEnumByValue(String value) {
        return Arrays.stream(ProcedureStepNodeFunctionEnum.values())
                .filter(stepFunctionEnum -> stepFunctionEnum.getValue().equals(value))
                .findFirst()
                .orElse(RECORD_WORK);
    }

    /**
     * 判断是否是工序换班或者工艺换班
     */
    public static Boolean changeTeamFlag(String value) {
        if (StrUtil.isBlank(value)){
            return false;
        }
        if (StrUtil.equals(value,ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue())
            || StrUtil.equals(value,ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue())){
            return true;
        }
        return false;
    }

    /**
     * 判断是否非记录节点
     * @param value
     * @return
     */
    public static Boolean notRecordNode(String value) {
        if (StrUtil.isBlank(value)){
            return false;
        }
        if (StrUtil.equals(value,ProcedureStepNodeFunctionEnum.PROCEDURE_CHANGE_TEAM.getValue())
                || StrUtil.equals(value,ProcedureStepNodeFunctionEnum.PROCESS_CHANGE_TEAM.getValue())
                || StrUtil.equals(value,ProcedureStepNodeFunctionEnum.INSPECT.getValue())){
            return true;
        }
        return false;
    }
}
