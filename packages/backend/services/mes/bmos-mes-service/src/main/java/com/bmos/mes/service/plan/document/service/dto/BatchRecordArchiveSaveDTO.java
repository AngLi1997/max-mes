package com.bmos.mes.service.plan.document.service.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 生成后的批记录进行保存所需要的参数
 */
@Getter
@Setter
public class BatchRecordArchiveSaveDTO {

    /**
     * 生成的path
     */
    private String path;

    /**
     * 透传的信息
     */
    private String extInfo;

}
