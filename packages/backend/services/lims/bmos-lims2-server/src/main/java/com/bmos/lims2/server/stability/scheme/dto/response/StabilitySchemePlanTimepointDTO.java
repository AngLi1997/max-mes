package com.bmos.lims2.server.stability.scheme.dto.response;

import lombok.Data;

import java.util.List;

/**
 * 稳定性方案检验计划时间点响应DTO
 */
@Data
public class StabilitySchemePlanTimepointDTO {

    private Long id;

    private Long planId;

    private Long versionId;

    private Integer timeValue;

    private String timeUnit;

    private String sampleAmount;

    private String sampleUnit;

    /**
     * 是否全选方案配置中的所有分析项
     */
    private Boolean selectAll;

    /**
     * 分析项引用列表（selectAll=false 时有效）
     */
    private List<ParamRefDTO> paramRefs;

    private Integer sort;

    @Data
    public static class ParamRefDTO {

        private Long id;

        /** 分析项配置ID */
        private Long parameterConfigId;

        /** 原始分析项ID */
        private Long parameterId;

        /** 分析项编码 */
        private String parameterCode;

        /** 检验项目配置ID */
        private Long itemConfigId;

        /** 检验项目ID */
        private Long inspectItemId;

        /** 检验项目编码 */
        private String inspectItemCode;
    }
}
