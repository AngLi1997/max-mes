package com.bmos.lims2.server.inspect.order.dto;

import com.bmos.lims2.common.enums.InspectionOrderStatusEnum;
import com.bmos.lims2.server.platform.util.UserUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单DTO
 *
 * @author yigaohui
 * @since 2025/01/27 15:30
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("检验单数据对象")
public class InspectionOrderDTO {

    @ApiModelProperty("检验单ID")
    private Long id;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品ID")
    private Long materialId;

    @ApiModelProperty("物料分类ID")
    private Long categoryId;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;


    @ApiModelProperty("检品规格")
    private String materialSpec;

    @ApiModelProperty("检品单位ID")
    private Long unitId;

    @ApiModelProperty("检品单位名称")
    private String unitName;

    @ApiModelProperty("请验人")
    private String requestUserId;

    @Getter(AccessLevel.NONE)
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

    @ApiModelProperty("单据状态（请验阶段）")
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

    @ApiModelProperty("自定义字段值列表")
    private List<CustomFieldValueDTO> customFields;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("终止原因")
    private String terminateReason;

    @ApiModelProperty("终止时间")
    private LocalDateTime terminateTime;

    @ApiModelProperty("检验结束时间")
    private LocalDateTime orderEndTime;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("更新人") 
    private String updateBy;

    @ApiModelProperty("取样计划列表")
    private List<InspectionSamplingDTO> samplingList;

    @ApiModelProperty("是否需要留样")
    private Boolean retentionRequired;

    @ApiModelProperty("有效期至（留样时必填）")
    private LocalDate retentionExpiryDate;

    @ApiModelProperty("检验单来源（REGULAR:常规请验 STABILITY:稳定性考察）")
    private com.bmos.lims2.common.enums.InspectionOrderSourceEnum schemeSource;

    @ApiModelProperty("是否已完成")
    private Boolean finished;

    @ApiModelProperty("完成时间")
    private LocalDateTime finishedTime;

    @ApiModelProperty("是否已取样（存在 sampled=true 的样品）")
    private Boolean hasSampled;

    public String getRequestUserName() {
        if (requestUserId == null) {
            return null;
        }
        if ("system".equalsIgnoreCase(requestUserId)) {
            return "system";
        }
        return UserUtils.getUserDisplayName(requestUserId);
    }

    // 注意：样品信息由样品管理功能维护，请验阶段不包含样品列表
}