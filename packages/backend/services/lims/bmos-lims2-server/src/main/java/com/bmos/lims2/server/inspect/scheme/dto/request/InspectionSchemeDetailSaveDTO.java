package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.lims2.server.inspect.scheme.dto.request.InspectionSchemeItemSaveDTO;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * 检验方案明细保存请求DTO
 * 正确的业务层级：检验方案明细 → 检验项目配置 → 分析项配置
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeDetailSaveDTO {

    /**
     * 检品ID
     */
    @NotNull(message = "检品ID不能为空")
    private Long materialId;

    /**
     * 实验包ID
     */
    @NotNull(message = "实验包ID不能为空")
    private Long packageId;

    /**
     * 检验项目配置列表
     */
    @Valid
    private List<InspectionSchemeItemSaveDTO> inspectionItems;
} 