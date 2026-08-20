package com.bmos.mes.service.platform.print.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/14 18:19
 */
@Data
@ApiModel("批量打印标签参数(根据设备id)")
public class PrintCommonDTOBatchByDeviceId {

    /**
     * 设备id
     */
    @ApiModelProperty(value = "设备id", example = "1")
    @NotNull
    private Long deviceId;

    /**
     * 场景id
     */
    @ApiModelProperty(value = "场景id", example = "1")
    private Long sceneId;

    /**
     * 参数列表
     */
    @ApiModelProperty(value = "参数(对象列表)")
    private List<Map<String, Object>> body;
}
