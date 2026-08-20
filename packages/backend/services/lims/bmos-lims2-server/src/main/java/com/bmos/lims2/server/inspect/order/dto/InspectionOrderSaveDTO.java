package com.bmos.lims2.server.inspect.order.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单保存DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("检验单保存数据对象")
public class InspectionOrderSaveDTO {

    @ApiModelProperty("检验单ID（编辑时传入）")
    private Long id;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检验方案ID")
    private Long schemeId;

    @ApiModelProperty("检验方案版本ID")
    private Long schemeVersionId;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("生产日期")
    private LocalDateTime productionDate;

    @ApiModelProperty("请验单模板ID")
    private Long templateId;

    @ApiModelProperty("自定义字段值列表")
    private List<CustomFieldValueDTO> customFields;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("取样信息列表")
    private List<InspectionSamplingSaveDTO> samplingList;

    @ApiModelProperty("是否需要留样")
    private Boolean retentionRequired;

    @ApiModelProperty("有效期至（留样时必填）")
    private LocalDate retentionExpiryDate;
}