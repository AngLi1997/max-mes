package com.bmos.mes.service.lotrelease.manage.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/27 19:26
 */
@Data
@ApiModel("批签发生成批次引用预览VO")
public class LotReleaseGeneratePreviewVO {

    private Long id;

    private Long planId;

    private String productName;

    private String processVersion;

    private String processName;

    private String batchNo;

    private LocalDateTime startTime;
}
