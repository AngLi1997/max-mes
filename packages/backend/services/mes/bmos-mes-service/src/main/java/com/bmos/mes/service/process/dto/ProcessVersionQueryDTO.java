package com.bmos.mes.service.process.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class ProcessVersionQueryDTO {

    /**
     * 版本号
     */
    private String version;

    /**
     * 工艺id
     */
    private Long processId;

}
