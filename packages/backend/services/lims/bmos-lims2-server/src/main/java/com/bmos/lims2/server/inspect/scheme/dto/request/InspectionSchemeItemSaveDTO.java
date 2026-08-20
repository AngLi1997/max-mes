package com.bmos.lims2.server.inspect.scheme.dto.request;

import com.bmos.lims2.common.enums.ItemDurationUnitEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 检验方案检验项目配置保存DTO
 * 正确的业务层级：检验方案明细 → 检验项目配置 → 分析项配置
 *
 * @author yigaohui
 * @since 2025/01/21 10:30
 */
@Data
public class InspectionSchemeItemSaveDTO {

    /**
     * 检验项目ID
     */
    @NotNull(message = "检验项目ID不能为空")
    private Long inspectItemId;

    /**
     * 是否必检
     */
    private Boolean isRequired = true;

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


    private List<Long> teams;

    /**
     * 检验项目下的分析项配置列表
     */
    @Valid
    private List<InspectionSchemeParameterSaveDTO> parameters;
}