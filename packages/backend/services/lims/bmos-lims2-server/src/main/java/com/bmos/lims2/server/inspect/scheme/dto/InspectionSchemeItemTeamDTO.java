package com.bmos.lims2.server.inspect.scheme.dto;

import lombok.Data;

import java.util.List;

/**
 * 检验方案分析项配置DTO
 * 注意：使用Parameter命名以保持与InspectParameter一致
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeItemTeamDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的检验项目配置ID
     */
    private Long itemConfigId;

    /**
     * 分析项ID
     */
    private Long parameterId;

    private Long teamId;
}