package com.bmos.lims2.server.inspect.scheme.dto.response;

import com.bmos.lims2.server.inspect.scheme.dto.InspectionSchemeItemDTO;
import lombok.Data;

import java.util.List;

/**
 * 检验方案明细响应DTO
 * 正确的业务层级：检验方案明细 → 检验项目配置 → 分析项配置
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeDetailDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的版本ID
     */
    private Long versionId;

    /**
     * 检品ID
     */
    private Long materialId;

    /**
     * 检品名称
     */
    private String materialName;

    /**
     * 检品编码
     */
    private String materialCode;

    /**
     * 实验包ID
     */
    private Long packageId;

    /**
     * 实验包名称
     */
    private String packageName;

    /**
     * 实验包编码
     */
    private String packageCode;

    /**
     * 取样量
     */
    private String samplingAmount;

    /**
     * 取样单位
     */
    private String samplingUnit;

    /**
     * 检验项目配置列表
     */
    private List<InspectionSchemeItemDTO> inspectionItems;
} 