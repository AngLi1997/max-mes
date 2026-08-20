package com.bmos.mes.service.lotrelease.manage.dto;

import lombok.Data;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/8/28 11:10
 */
@Data
public class LotReleaseUpdateExcelFileDTO {

    private Long lotReleaseId;

    private String remark;

    private String fileUrl;
}
