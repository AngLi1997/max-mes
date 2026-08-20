package com.bmos.mes.service.weigh.centre2.execute.controller.vo;

import com.bmos.mes.common.enums.CategoryInfoTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 称量是否满足称量需求的目标量
 */
@Data
public class TicketRequirementEnoughVO {

    /**
     * 称量所产生的物料件号
     */
    private String storageMaterialNo;

    /**
     * 是否满足目标量
     */
    private Boolean requirementEnough;

    /**
     * 是否超出余料目标量
     */
    private Boolean outOddmentTargetEnough;

    /**
     * 工单是否满足完成条件
     */
    private Boolean ticketCompleteCondition;

    /**
     * 是否进入余料称量
     */
    private Boolean oddmentEnough;

    /**
     * 所称的物料类型
     */
    private CategoryInfoTypeEnum categoryInfoType;

    /**
     * 当前需求/余料的称量记录
     */
    private List<WeighRequirementRecordVO> weighRequirementRecordVOList;

}
