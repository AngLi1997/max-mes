package com.bmos.lims2.server.task.dto;

import com.bmos.lims2.common.enums.InspectionOrderSourceEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 任务检验方案配置DTO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
public class TaskSchemeConfigDTO {

    /**
     * 任务ID
     */
    @ApiModelProperty("任务ID")
    private Long taskId;

    /**
     * 检验项目ID
     */
    @ApiModelProperty("检验项目ID")
    private Long inspectItemId;

    /**
     * 检验方案版本ID
     */
    @ApiModelProperty("检验方案版本ID")
    private Long schemeVersionId;

    /**
     * 来源类型（REGULAR/STABILITY），用于分支查询对应班组表
     */
    @ApiModelProperty("来源类型")
    private InspectionOrderSourceEnum schemeSource;
}
