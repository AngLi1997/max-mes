package com.bmos.lims2.server.inspect.scheme.dto.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 检验方案响应DTO
 *
 * @author makejava
 * @since 2024-03-20 10:00:00
 */
@Data
public class InspectionSchemeDTO {

    /**
     * 主键ID
     */
    private Long id;



    /**
     * 方案名称
     */
    private String name;

    /**
     * 方案描述
     */
    private String description;

    /**
     * 当前生效版本号
     */
    private String activeVersionNo;

    /**
     * 当前生效版本ID
     */
    private Long activeVersionId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 创建人名称
     */
    private String createByName;

    /**
     * 物料ID
     */
    private Long materialId;

    /**
     * 物料编码
     */
    private String materialCode;

    /**
     * 物料名称（从关联表查询）
     */
    private String materialName;


    @ApiModelProperty("单位id")
    private Long materialUnitId;

    @ApiModelProperty("单位名称")
    private String materialUnitName;

    /**
     * 实验包ID
     */
    private Long packageId;

    /**
     * 实验包编码
     */
    private String packageCode;

    /**
     * 实验包名称（从关联表查询）
     */
    private String packageName;
} 