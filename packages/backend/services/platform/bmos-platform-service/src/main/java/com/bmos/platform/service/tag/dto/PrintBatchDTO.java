package com.bmos.platform.service.tag.dto;

import com.bmos.platform.service.tag.enums.PrinterDpi;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("批量打印标签DTO")
@Data
public class PrintBatchDTO {

    /**
     * 打印机ip
     */
    @ApiModelProperty(value = "打印机ip", example = "127.0.0.1")
    private String printerIp;

    /**
     * 打印机端口
     */
    @ApiModelProperty(value = "打印机端口", example = "9100")
    private Integer printerPort;

    /**
     * 打印机dpi
     */
    @ApiModelProperty(value = "打印机dpi(203/300)", example = "203")
    private Integer dpi = PrinterDpi.DPI_203.getDpi();

    /**
     * 场景id
     */
    @ApiModelProperty(value = "场景id", example = "1")
    private Long sceneId;

    /**
     * 参数
     */
    @ApiModelProperty(value = "参数")
    private List<Map<String, Object>> bodyList;

}
