package com.bmos.mes.service.weigh.centre2.execute.service.dto;

import com.bmos.mybatis.page.BasePage;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeighTicketPageDTO extends BasePage {
    /**
     * 物料名称或编码
     */
    private String material;
    /**
     * 称量中心名称或编码
     */
    private String centre;
    /**
     * 工单号
     */
    private String ticketNo;

} 