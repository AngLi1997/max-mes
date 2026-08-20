package com.bmos.mes.service.weigh.centre2.execute.controller.vo;

import com.bmos.mes.common.enums.weigh.centre2.TicketWeighStatusEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class WeighTicketDetailVO {
    /**
     * 工单ID
     */
    private Long id;

    /**
     * 工单编号
     */
    private String ticketNo;
    /**
     * 物料合并编码
     */
    private String materialMergeCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料批次id
     */
    private Long storageMaterialBatchId;
    /**
     * 物料批号
     */
    private String materialBatchNo;
    /**
     * 需求总量
     */
    private BigDecimal requirementQuantity;
    /**
     * 称量中心编码
     */
    private String centreCode;
    /**
     * 称量中心名称
     */
    private String centreName;

    /**
     * 称量中心工位id集合
     */
    private List<Long> stationIdList;
    /**
     * 单位id
     */
    private Long unitId;
    /**
     * 单位名称
     */
    private String unitName;
    /**
     * 需求明细列表
     */
    private List<WeighRequirementVO> requirements;
    /**
     * 需求内已称量的量 不包含余料称量
     */
    private BigDecimal weighRequirementWeighedQuantity;
    /**
     * 工单已称量的量
     */
    private BigDecimal weighedQuantity;
    /**
     * 工单未称量的量
     */
    private BigDecimal notWeighedQuantity;
    /**
     * 称量状态
     */
    private TicketWeighStatusEnum ticketWeighStatus;

    /**
     * 添加的物料件的总量
     */
    private BigDecimal quality;

    /**
     * 是否进入余料称量
     */
    private Boolean oddmentEnough;

    /**
     * 绑定的称量人id
     */
    private String weighUserId;

    /**
     * 绑定的签名人id
     */
    private String signUserId;

    /**
     * 是否已经全部称完
     */
    private Boolean enoughCompleteCondition;
} 