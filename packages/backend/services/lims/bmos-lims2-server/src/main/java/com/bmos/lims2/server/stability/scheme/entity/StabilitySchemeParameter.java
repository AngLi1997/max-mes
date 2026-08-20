package com.bmos.lims2.server.stability.scheme.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bmos.lims2.common.enums.ExecuteMethodEnum;
import com.bmos.mybatis.dataobject.BaseDO;
import lombok.Getter;
import lombok.Setter;

/**
 * 稳定性方案分析项配置实体类
 *
 * @author yigaohui
 * @since 2025-03-17
 */
@Getter
@Setter
@TableName("lm_stability_scheme_parameter")
public class StabilitySchemeParameter extends BaseDO {

    private Long schemeId;

    private Long versionId;

    private Long itemConfigId;

    private Long inspectItemId;

    private Long parameterId;

    private String standardRule;

    private Boolean isExecutable;

    private Boolean isReportable;

    private ExecuteMethodEnum executeMethod;

    /**
     * 最终判定表达式
     */
    private String finalExpression;

    /**
     * 分析方法记录ID（ELN执行时绑定）
     */
    private Long recordId;

    /**
     * 分析方法编码
     */
    private String recordCode;

    /**
     * 分析方法版本ID
     */
    private Long recordVersionId;

    /**
     * 分析方法记录项ID
     */
    private Long recordItemId;
}
