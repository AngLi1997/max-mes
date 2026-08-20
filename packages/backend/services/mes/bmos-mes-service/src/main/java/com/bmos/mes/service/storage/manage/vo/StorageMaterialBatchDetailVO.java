package com.bmos.mes.service.storage.manage.vo;

import com.bmos.mes.common.enums.material.MaterialQualityStatusEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@ApiModel("物料批次详情VO")
@Data
public class StorageMaterialBatchDetailVO {

    @ApiModelProperty("物料合并编码")
    private String materialMergeCode;

    @ApiModelProperty("物料名称")
    private String materialName;

    @ApiModelProperty("物料批号")
    private String materialBatchNo;

    /**
     * 生产日期
     */
    @ApiModelProperty(value = "生产日期", example = "2024-03-29")
    private LocalDate produceDate;

    /**
     * 有效日期
     */
    @ApiModelProperty(value = "有效日期", example = "2024-03-29")
    private LocalDate expiredDate;

    @ApiModelProperty("物料批次自定义字段信息列表")
    private List<MaterialBatchFieldVO> fieldList;


    /**
     * 水分(%)
     */
    @ApiModelProperty(value = "水分(%)", example = "1.000")
    private BigDecimal hydration;

    /**
     * 无水含量(%)
     */
    @ApiModelProperty(value = "无水含量(%)", example = "1.000")
    private BigDecimal noHydrationContent;

    /**
     * 报告单编号
     */
    @ApiModelProperty(value = "报告单编号", example = "123456")
    private String reportNo;

    /**
     * 放行单编号
     */
    @ApiModelProperty(value = "放行单编号", example = "123456")
    private String licenceNo;

    /**
     * 原厂批号
     */
    @ApiModelProperty(value = "原厂批号", example = "2310001")
    private String factoryBatchNo;

    @ApiModelEnumProperty(value = "物料批次质量状态", enumClass = MaterialQualityStatusEnum.class)
    private MaterialQualityStatusEnum qualityStatus;
}
