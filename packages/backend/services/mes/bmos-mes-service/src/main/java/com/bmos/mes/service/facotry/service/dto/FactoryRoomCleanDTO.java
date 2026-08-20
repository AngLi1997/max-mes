package com.bmos.mes.service.facotry.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 房间清场DTO
 */
@Getter
@Setter
@ApiModel("房间清场执行DTO")
@Validated
public class FactoryRoomCleanDTO extends RoomCleanCheckSaveDTO{

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;


    /**
     * 工序id
     */
    @ApiModelProperty("工序id")
    private Long procedureModelId;

    /**
     * 工序名称
     */
    @ApiModelProperty("工序名称")
    private String procedureName;

    /**
     * 清场执行人id
     */
    @ApiModelProperty("清场执行人id")
    @NotNull
    private String operatorId;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull
    private String beginTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull
    private String endTime;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", required = true)
    @NotNull
    private String verifierId;

    /**
     * 复核时间
     */
    @ApiModelProperty(value = "复核时间", required = true)
    private String verifyTime;

    /**
     * 清场有效期
     */
    @ApiModelProperty(value = "清场有效期", required = true)
    @NotNull
    private String expireTime;


    @ApiModelProperty(value = "产品id", required = true)
    private Long productId;

    @ApiModelProperty(value = "工序模型id")
    private Long procedureId;

}
