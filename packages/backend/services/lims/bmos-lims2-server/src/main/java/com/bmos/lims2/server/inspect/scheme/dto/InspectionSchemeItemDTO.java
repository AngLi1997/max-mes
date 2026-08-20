package com.bmos.lims2.server.inspect.scheme.dto;

import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import lombok.Data;

import java.util.List;

/**
 * 检验方案检验项目配置DTO
 * 新的业务层级：检验方案 → 版本 → 检验项目配置 → 分析项配置
 *
 * @author yigaohui
 * @since 2025/01/21 10:30
 */
@Data
public class InspectionSchemeItemDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的方案ID
     */
    private Long schemeId;

    /**
     * 关联的版本ID
     */
    private Long versionId;

    /**
     * 实验包id
     */
    private Long packageId;

    /**
     * 检验项目ID
     */
    private Long inspectItemId;

    /**
     * 检验项目编码
     */
    private String inspectItemCode;

    /**
     * 检验项目名称
     */
    private String inspectItemName;

    /**
     * 是否必检
     */
    private Boolean isRequired;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    private String remark;


    private Integer duration;

    private ItemDurationUnitEnum timeUnit;

    private List<InspectionSchemeParameterDTO> parameters;

    private List<Long> teams;
}