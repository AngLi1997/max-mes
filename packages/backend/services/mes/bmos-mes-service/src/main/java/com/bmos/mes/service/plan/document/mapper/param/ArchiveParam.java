package com.bmos.mes.service.plan.document.mapper.param;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArchiveParam {

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 生产批号
     */
    private String batchNo;

    /**
     * 状态
     */
    private Integer status;

}
