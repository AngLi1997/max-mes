package com.bmos.lims2.server.inspect.scheme.dto.request;

import lombok.Data;

/**
 * @Description: 新增版本-从现有版本复制 请求参数
 * @Author: yigaohui
 * @Date: 2025/09/03 10:30
 */
@Data
public class InspectionSchemeVersionCopyDTO {

    /**
     * 源版本ID（从该版本复制配置）
     */
    private Long sourceVersionId;

    /**
     * 新版本号
     */
    private String newVersionNo;

    /**
     * 版本描述（可选）
     */
    private String description;
}


