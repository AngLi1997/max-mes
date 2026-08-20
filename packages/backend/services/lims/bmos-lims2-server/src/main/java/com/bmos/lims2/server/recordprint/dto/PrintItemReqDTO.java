package com.bmos.lims2.server.recordprint.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 打印项请求DTO（任务ID + 绑定ID）
 * @Author: yigaohui
 * @Date: 2025/11/25 10:20
 */
@Getter
@Setter
public class PrintItemReqDTO {

    /**
     * 任务ID
     */
    private Long taskId;
}


