package com.bmos.mes.service.weigh.centre2.execute.service.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.RequestParam;

@Data
public class TicketBindOperatorDTO {
    /**
     * 工单id
     */
    private Long ticketId;

    /**
     * 绑定用户id
     */
    private String userId;

    /**
     * 签名用户id
     */
    private String signUser;

    /**
     * 备注
     */
    private String remark;
}
