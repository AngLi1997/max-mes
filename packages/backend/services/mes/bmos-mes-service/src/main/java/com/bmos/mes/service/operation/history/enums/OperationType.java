package com.bmos.mes.service.operation.history.enums;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OperationType implements CommonEnum<String> {

    SAVE("新增", "save"),
    UPDATE("修改", "update"),
    DELETE("删除", "del"),
    NULLIFY("作废","nullify"),
    CONFIRM("确认","confirm"),
    REDACT("编辑","redact"),
    HANDLE("处理","handle"),
    FORMULA_CONFIG("公式配置","formula"),
    FORMULA_CLEAR("公式清除","formulaClear"),
    PROCESS_UPDATE("编辑", "processUpdate"),
    SUBMIT_AUDIT("提交审核", "submitAudit"),
    REJECT_AUDIT("审核不通过", "rejectAudit"),
    APPROVE_AUDIT("审核通过", "approveAudit"),
    BACK_AUDIT("审核退回", "backAudit"),
    VALID("立即生效", "valid"),
    DIRECT_VALID("直接生效", "direct_valid"),
    INVALID("停用", "invalid"),
    PRINT("打印","print"),
    START_AUDIT("启用审核", "startFlow"),
    view("预览","view"),
    FORMULA_EDIT("配方编辑","formulaEdit"),
    BATCH_RELEASE_GENERATE("批签发生成","batchReleaseGenerate"),
    BATCH_RELEASE_REGENERATE("重新生成", "batchReleaseReGenerate"),
    RE_INVESTIGATE("重新调查", "reInvestigate"),
    CHANGE_CALENDAR("日历调整","changeCalendar"),
    FRESH_EDIT("重新编辑","fresh_edit"),
    ENABLE("启用", "enable")
    ;

    private final String name;

    @EnumValue
    private final String value;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getValue() {
        return value;
    }
}
