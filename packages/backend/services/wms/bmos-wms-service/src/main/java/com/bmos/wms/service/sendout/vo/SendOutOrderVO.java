package com.bmos.wms.service.sendout.vo;

import com.bmos.common.validate.EnumValidate;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import com.bmos.wms.common.enums.sendout.SendOrderType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 发料工单vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/15 17:53
 */
@Data
@ApiModel("发料工单vo")
public class SendOutOrderVO {

    /**
     * id
     */
    @ApiModelProperty(value = "id", example = "1")
    private Long id;

    /**
     * 领料计划id
     */
    @ApiModelProperty(value = "领料计划id", example = "1")
    private Long requisitionPlanId;

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id", example = "1")
    private Long productId;

    /**
     * 产品编码
     */
    @ApiModelProperty(value = "产品编码", example = "C01001")
    private String productCode;

    /**
     * 产品名称
     */
    @ApiModelProperty(value = "产品名称", example = "人血白蛋白")
    private String productName;

    /**
     * 产品规格
     */
    @ApiModelProperty(value = "产品规格", example = "10g/支")
    private String productSpecification;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id", example = "1")
    private Long processId;

    /**
     * 工艺名称
     */
    @ApiModelProperty(value = "工艺名称", example = "投浆工艺")
    private String processName;

    /**
     * 生产批号
     */
    @ApiModelProperty(value = "生产批号", example = "20230401")
    private String batchNo;

    /**
     * 领料单号
     */
    @ApiModelProperty(value = "领料单号", example = "20230401")
    private String pullOrderNo;

    /**
     * 计划人id
     */
    @ApiModelProperty(value = "计划人id", example = "1")
    private String submitterId;

    /**
     * 计划人名称
     */
    @ApiModelProperty(value = "计划人名称", example = "张三")
    private String submitterName;

    /**
     * 计划时间
     */
    @ApiModelProperty(value = "计划时间", example = "2023-04-01 00:00:00")
    private LocalDateTime submitTime;


    /**
     * 发料工单类型
     */
    @ApiModelEnumProperty(value = "发料工单类型", enumClass = SendOrderType.class)
    @EnumValidate(SendOrderType.class)
    private SendOrderType sendOrderType;
}
