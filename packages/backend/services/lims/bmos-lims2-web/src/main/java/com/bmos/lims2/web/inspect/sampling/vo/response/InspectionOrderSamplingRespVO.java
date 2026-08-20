package com.bmos.lims2.web.inspect.sampling.vo.response;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 请验单取样信息响应VO
 *
 * @author yigaohui
 * @since 2025/01/29 16:00
 */
@Data
@ApiModel("请验单取样信息响应")
public class InspectionOrderSamplingRespVO {

    @ApiModelProperty("检验单ID")
    private Long id;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("请验人")
    private String requestUserId;

    @ApiModelProperty("请验人名称")
    private String requestUserName;

    @ApiModelProperty("请验时间")
    private LocalDateTime requestTime;

    @ApiModelProperty("检验方案版本ID")
    private Long schemeVersionId;

    @ApiModelProperty("检验方案名称")
    private String schemeName;

    @ApiModelProperty("检验方案版本号")
    private String schemeVersion;

    @ApiModelProperty("标准单位ID")
    private Long unitId;

    @ApiModelProperty("标准单位名称")
    private String unitName;

    @ApiModelProperty("单据状态")
    private InspectionOrderStatusEnum orderStatus;

    @ApiModelProperty("单据状态描述")
    private String orderStatusDesc;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("生产日期")
    private LocalDateTime productionDate;

    @ApiModelProperty("请验单模板ID")
    private Long templateId;

    @ApiModelProperty("请验单模板名称")
    private String templateName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("样品列表")
    private List<SampleRespVO> samples;

    @ApiModelProperty("自定义字段值列表")
    private List<CustomFieldValueRespVO> customFields;

    /**
     * 自定义字段值响应VO
     */
    @Data
    @ApiModel("自定义字段值响应")
    public static class CustomFieldValueRespVO {
        @ApiModelProperty("字段ID")
        private Long fieldId;

        @ApiModelProperty("字段名称")
        private String fieldName;

        @ApiModelProperty("字段值")
        private String fieldValue;

        @ApiModelProperty("字段类型")
        private String fieldType;
    }
}
