package com.bmos.lims2.web.inspect.parameter.vo.resp;

import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 分析项(BmExperimentAnalyze)实体类
 *
 * @author makejava
 * @since 2024-03-02 12:43:37
 */
@Getter
@Setter
public class InspectParameterVO extends BaseDO {

    /**
     * 分析项编码
     */
    private String code;
    /**
     * 分析项名称
     */
    private String name;
    /**
     * 当前分析项默认标准规定
     */
    private String standard;

    /**
     * 分析项结果类型
     */
    private AnalyzeResultTypeEnum resultType;
}

