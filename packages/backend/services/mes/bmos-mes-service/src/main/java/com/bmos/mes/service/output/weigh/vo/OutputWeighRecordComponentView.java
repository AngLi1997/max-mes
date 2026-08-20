package com.bmos.mes.service.output.weigh.vo;

import com.bmos.mes.common.enums.ingredient.WeighSignStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 产出称量组件信息回显
 * @author liang
 * @version 1.0.0
 * @date 2024/5/13 14:54
 */
@Data
public class OutputWeighRecordComponentView {

    /**
     * 物料id
     */
    private Long materialId;

    /**
     * 物料名称
     */
    private String materialName;

    /**
     * 物料编码
     */
    private String mergeCode;

    /**
     *  物料规格
     */
    private String specification;

    /**
     * 物料批号
     */
    private String materialBatchNo;

    /**
     * 物料件编号
     */
    private String materialNo;

    /**
     * 物料量
     */
    private BigDecimal quantity;

    /**
     * 皮重
     */
    private BigDecimal tareWeight;

    /**
     * 毛重
     */
    private BigDecimal grossWeight;

    /**
     * 净重
     */
    private BigDecimal netWeight;

    /**
     * 单位名称
     */
    private String unit;

    /**
     * 称量单位id
     */
    private Long unitId;

    /**
     * 称量人名称
     */
    private String weigherName;

    /**
     * 复核人名称
     */
    private String reCheckerName;

    /**
     * 称量时间
     */
    private LocalDateTime weighTime;

    /**
     * 签名状态
     */
    private WeighSignStatus weighSignStatus;
}
