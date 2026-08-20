package com.bmos.wms.service.sendout.vo;

import lombok.Data;

/**
 * 发料单项目vo
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/16 13:38
 */
@Data
public class SendOrderItemVO {

    /**
     * id
     */
    private Long businessId;

    /**
     * 货品名称
     */
    private String cargoName;

    /**
     * 货品编码
     */
    private String cargoCode;

    /**
     * 合并编码
     */
    private String mergeCode;

    /**
     * 货品规格
     */
    private String cargoSpecification;

    /**
     * 批次号
     */
    private String batchNo;

}
