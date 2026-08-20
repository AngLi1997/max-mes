package com.bmos.mes.common.enums.plan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.bmos.common.base.enums.CommonEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 批记录操作枚举
 */
@Getter
@AllArgsConstructor
public enum BatchRecordArchiveOperateTypeEnum implements CommonEnum<Integer> {

    /**
     * 新增
     */
    RE_GENERATE(830301,"重新生成"),

    /**
     * 上传
     */
    UPLOAD(830302, "上传"),

    /**
     * 下载
     */
    DOWNLOAD(830303, "下载"),

    /**
     * 提交审批
     */
    AUDIT(830304, "提交审批"),

    /**
     * 审批完成
     */
    AUDIT_COMPLETE(830305, "审批完成"),

    /**
     * 作废
     */
    SCRAP(830306, "作废"),

    /**
     * 批记录生成
     */
    GENERATE(830307, "批记录生成"),

    /**
     * 批记录生成
     */
    AUTO_GENERATE(830308, "自动生成"),

    /**
     * 模板验证
     */
    VERIFIER(830309, "验证"),
    /**
     * 确认生效
     */
    EFFECTIVE(830310, "确认生效")
    ;
    @EnumValue
    private Integer value;

    private String name;


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public static BatchRecordArchiveOperateTypeEnum getEnumByValue(Integer value) {
        for (BatchRecordArchiveOperateTypeEnum typeEnum : BatchRecordArchiveOperateTypeEnum.values()) {
            if (typeEnum.getValue().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }

}
