package com.bmos.lims2.web.inspect.order.vo.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单保存请求VO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Data
@ApiModel("检验单保存请求")
public class InspectionOrderSaveVO {

    @ApiModelProperty("检验单ID（编辑时传入）")
    private Long id;

    @ApiModelProperty(value = "检品ID", required = true)
    @NotNull(message = "检品ID不能为空")
    private Long materialId;

    @ApiModelProperty(value = "检验方案版本ID", required = true)
    @NotNull(message = "检验方案版本ID不能为空")
    private Long schemeVersionId;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("请验单模板ID")
    private Long templateId;

    @ApiModelProperty("自定义字段值列表")
    @Valid
    private List<CustomFieldValueVO> customFields;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("取样信息列表")
    @Valid
    private List<InspectionSamplingSaveVO> samplingList;

    @ApiModelProperty("是否需要留样")
    private Boolean retentionRequired;

    @ApiModelProperty("有效期至（留样时必填）")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate retentionExpiryDate;
}