package com.bmos.platform.service.factory.controller.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

/**
 * 房间配置环境参数VO
 *
 * @author makejava
 * @since 2024-12-30 10:04:53
 */
@Data
@ApiModel("房间配置环境参数VO")
@Validated
public class FactoryRoomEnvPropertyAddVO {
    private static final long serialVersionUID = 593776516580148780L;
    /**
     * 房间id
     */
    @ApiModelProperty("房间id")
    @NotNull
    private Long roomId;

    /**
     * 设备id
     */
    @NotNull
    @ApiModelProperty("设备id")
    private Long equipmentId;
    /**
     * 设备数据编码
     */
    @ApiModelProperty("环境参数code")
    @NotNull
    private String equipmentDataPropertyCode;

    @ApiModelProperty("环境参数编码")
    @NotNull
    private String envPropertyCode;

}

