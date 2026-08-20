package com.bmos.mes.inspect.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 检验项结果
 */
@Getter
@Setter
public class InspectProgramResultDTO {

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
