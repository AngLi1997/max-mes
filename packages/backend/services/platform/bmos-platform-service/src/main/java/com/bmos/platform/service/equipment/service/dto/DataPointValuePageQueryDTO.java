package com.bmos.platform.service.equipment.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author yigaohui
 * @date 2024/4/23
 **/
@Data
public class DataPointValuePageQueryDTO extends BasePage {
    @ApiModelProperty("设备id")
    private Long equipmentId;

    @ApiModelProperty("采集项id")
    private Long acquisitionPointId;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;
}
