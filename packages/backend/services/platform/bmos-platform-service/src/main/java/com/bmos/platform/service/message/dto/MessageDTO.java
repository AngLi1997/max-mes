package com.bmos.platform.service.message.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @className: MessageVO
 * @author: yigaohui
 * @date: 2025/1/8 13:54
 * @Version: 1.0
 * @description:
 */

@Data
public class MessageDTO {

    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("内容")
    private String content;

    @ApiModelProperty("时间")
    private LocalDateTime time;
}
