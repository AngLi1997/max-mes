package com.bmos.lims2.server.inspect.scheme.dto.response;

import com.bmos.lims2.common.enums.InspectionSchemeVersionStatusEnum;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 检验方案版本响应DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeVersionDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 关联的检验方案ID
     */
    private Long schemeId;

    /**
     * 版本号
     */
    private String versionNo;

    /**
     * 版本状态：EDITING-编辑中, APPROVING-审批中, ACTIVE-生效, INACTIVE-失效
     */
    private InspectionSchemeVersionStatusEnum status;

    /**
     * 关联的审批流程ID
     */
    private String processInstanceId;

    /**
     * 父版本ID
     */
    private Long parentVersionId;

    /**
     * 父版本号
     */
    private String parentVersionNo;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建人名称
     */
    private String createByName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 更新人名称
     */
    private String updateByName;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("生效日期")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;
} 