package com.bmos.mes.service.weigh.centre2.execute.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 完成称量DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinishWeighDTO {

    /**
     * 称量需求ID
     */
    private Long requirementId;

    /**
     * 工单id
     */
    private Long ticketId;

    /**
     * 称量类型
     */
    private Integer weighType;

    /**
     * 完成称量按钮的签名
     */
    private String finishSignUser;
}
