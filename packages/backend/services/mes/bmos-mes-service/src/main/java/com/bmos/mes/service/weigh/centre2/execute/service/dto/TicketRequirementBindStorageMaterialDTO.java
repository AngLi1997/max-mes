package com.bmos.mes.service.weigh.centre2.execute.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class TicketRequirementBindStorageMaterialDTO {

    /**
     * 称量需求id
     */
    private Long requirementId;

    /**
     * 工单id
     */
    private Long ticketId;

    /**
     * 物料件id集合
     */
    List<Long> storageMaterialIds;
}
