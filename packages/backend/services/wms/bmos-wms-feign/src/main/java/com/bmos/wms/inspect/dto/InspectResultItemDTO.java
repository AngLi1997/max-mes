package com.bmos.wms.inspect.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 请验结果回传 - 检验项（与 mes-feign 同款）
 */
@Getter
@Setter
public class InspectResultItemDTO {

    /**
     * 检项代码
     */
    private String inspectProgramNo;

    /**
     * 已经转换的检项代码
     */
    private String alreadyConvertProgramNo;

    /**
     * 检验项名称
     */
    private String inspectProgramName;

    /**
     * 检验项结果
     */
    private String inspectResult;

    /**
     * 检验结论 例如合格/不合格
     */
    private String inspectConclusion;
}
