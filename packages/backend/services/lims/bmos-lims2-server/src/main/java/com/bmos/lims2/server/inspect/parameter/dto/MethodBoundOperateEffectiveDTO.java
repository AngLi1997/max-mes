package com.bmos.lims2.server.inspect.parameter.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description: 方法绑定的启用版本操作规程信息 DTO（含下载地址）
 * @Author: yigaohui
 * @Date: 2025/11/03 00:00
 */
@Getter
@Setter
public class MethodBoundOperateEffectiveDTO {

    private Long operateId;
    private String operateName;
    private Long versionId;
    private String versionNo;
    private String downloadUrl;
}


