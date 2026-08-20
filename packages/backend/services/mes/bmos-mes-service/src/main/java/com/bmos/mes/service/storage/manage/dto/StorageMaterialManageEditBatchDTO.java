package com.bmos.mes.service.storage.manage.dto;

import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 编辑物料批次dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/7 16:52
 */
@Data
@ApiModel("编辑物料批次dto")
public class StorageMaterialManageEditBatchDTO {

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1", required = true)
    @NotNull
    private Long storageMaterialBatchId;
    ;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "2024032901")
    @Length(max = 100)
    private String factoryBatchNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期", example = "2024-03-29")
    private LocalDate produceDate;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2024-03-29", required = true)
    @NotNull
    private LocalDate expiredDate;

    /**
     * 水分(%) 默认 0
     */
    @ApiModelProperty(value = "水分(%)", example = "999.9999")
    @DecimalMin("0")
    @DecimalMax("999.9999")
    private BigDecimal hydration;

    /**
     * 无水含量(%) 默认 100
     */
    @ApiModelProperty(value = "无水含量(%)", example = "999.9999")
    @DecimalMin("0")
    @DecimalMax("999.9999")
    private BigDecimal noHydrationContent;

    /**
     * 报告单编号
     */
    @ApiModelProperty(value = "报告单编号", example = "2024032901")
    @Length(max = 100)
    private String reportNo;

    /**
     * 放行单编号
     */
    @ApiModelProperty(value = "放行单编号", example = "2024032901")
    @Length(max = 100)
    private String licenceNo;

    /**
     * 原始编码
     */
    @ApiModelProperty(value = "原始编码", example = "WH050101")
    @Length(max = 100)
    private String originalCode;

    /**
     * 供应商
     */
    @ApiModelProperty(value = "供应商", example = "供应商")
    @Length(max = 100)
    private String supplier;

    /**
     * 生产商
     */
    @ApiModelProperty(value = "生产商", example = "生产商")
    @Length(max = 100)
    private String producer;

    /**
     * 原始编码
     */
    @ApiModelProperty(value = "原始编码", example = "WH050101")
    @Length(max = 100)
    private String originalBatchNo;

    @ApiModelEnumProperty(value = "物料批次质量状态", enumClass = MaterialQualityStatusEnum.class)
    private String qualityStatus;

    /**
     * 物料批次自定义字段
     */
    @ApiModelProperty(value = "物料批次自定义字段", example = "1", required = false)
    private List<MaterialBatchFieldDTO> materialBatchFieldDTOList;
}
