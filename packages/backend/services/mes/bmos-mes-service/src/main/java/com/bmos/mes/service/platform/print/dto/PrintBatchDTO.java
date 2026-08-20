package com.bmos.mes.service.platform.print.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.List;
import java.util.Map;

@ApiModel("批量打印DTO")
@Data
public class PrintBatchDTO {

    /**
     * 打印机ip
     */
    private String printerIp;

    /**
     * 打印机端口
     */
    private Integer printerPort;

    /**
     * 场景id
     */
    private Long sceneId;

    /**
     * 参数
     */
    private List<Map<String, Object>> bodyList;

}
