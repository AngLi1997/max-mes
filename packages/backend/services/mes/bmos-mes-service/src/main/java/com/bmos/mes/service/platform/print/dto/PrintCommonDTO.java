package com.bmos.mes.service.platform.print.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/3/14 18:19
 */
@Data
@ApiModel("打印标签参数")
public class PrintCommonDTO {

    /**
     * 打印机ip
     */
    @ApiModelProperty(value = "打印机ip", example = "192.168.112.7")
    @NotBlank
    private String printerIp;

    /**
     * 打印机端口
     */
    @ApiModelProperty(value = "打印机端口", example = "9100")
    @NotNull
    private Integer printerPort;

    /**
     * 场景id
     */
    @ApiModelProperty(value = "场景id", example = "1")
    private Long sceneId;

    /**
     * 参数
     */
    @ApiModelProperty(value = "参数(对象)")
    private Map<String, Object> body;
}
