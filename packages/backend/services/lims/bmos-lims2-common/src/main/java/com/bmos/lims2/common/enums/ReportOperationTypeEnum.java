package com.bmos.lims2.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.KeyValueEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 报告版本操作类型
 */
@Getter
@AllArgsConstructor
public enum ReportOperationTypeEnum implements KeyValueEnum<String> {

    CREATE("CREATE", "新增"),
    UPLOAD("UPLOAD", "上传"),
    DOWNLOAD("DOWNLOAD", "下载"),
    CONFIRM("CONFIRM", "确认"),
    APPROVAL_PASS("APPROVAL_PASS", "审批通过"),
    APPROVAL_REJECT("APPROVAL_REJECT", "审批不通过"),
    SET_DEFAULT("SET_DEFAULT", "设置默认"),
    VALIDATE("VALIDATE", "验证"),
    GENERATE("GENERATE", "生成"),
    VOID("VOID", "作废");

    @EnumValue
    private final String value;
    private final String name;
}


