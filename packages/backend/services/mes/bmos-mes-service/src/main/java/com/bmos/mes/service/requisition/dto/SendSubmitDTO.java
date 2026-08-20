package com.bmos.mes.service.requisition.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.mes.common.enums.requisition.SendOrderType;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 发料工单申请dto
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/12 18:19
 */
@Data
@ApiModel("发料工单申请dto")
public class SendSubmitDTO {

    /**
     * 领料计划id
     */
    @ApiModelProperty(value = "MES领料计划id", example = "1")
    @NotNull
    private Long requisitionPlanId;

    /**
     * 领料单号
     */
    @NotBlank
    @ApiModelProperty(value = "领料单号", example = "20230401")
    private String pullOrderNo;

    /**
     * 发料工单类型
     */
    @NotNull
    @ApiModelEnumProperty(value = "发料工单类型", enumClass = SendOrderType.class)
    @EnumValidate(SendOrderType.class)
    private Integer sendOrderType;

    /**
     * 产品名称
     */
    @NotBlank
    @ApiModelProperty(value = "产品名称", example = "人血白蛋白")
    private String productName;

    /**
     * 待发料批次/货品
     */
    @NotEmpty
    @Valid
    private List<SendSubmitQuantityDTO> pendingSendList;

    /**
     * 产品id
     */
    @NotNull
    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    /**
     * 产品编码
     */
    @NotBlank
    @ApiModelProperty(value = "产品编码", example = "C01001")
    private String productCode;

    /**
     * 产品规格
     */
    @NotBlank
    @ApiModelProperty(value = "产品规格", example = "10g/支")
    private String productSpecification;

    /**
     * 工艺id
     */
    @NotNull
    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    /**
     * 工艺名称
     */
    @NotBlank
    @ApiModelProperty(value = "工艺名称", example = "投浆工艺")
    private String processName;

    /**
     * 生产批号
     */
    @NotBlank
    @ApiModelProperty(value = "生产批号", example = "20230401")
    private String batchNo;

    /**
     * 计划人
     */
    @NotNull
    @ApiModelProperty(value = "计划人id", example = "1")
    private Long submitterId;
}
