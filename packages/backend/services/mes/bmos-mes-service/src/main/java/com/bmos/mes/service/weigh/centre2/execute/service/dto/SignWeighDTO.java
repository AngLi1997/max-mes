package com.bmos.mes.service.weigh.centre2.execute.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SignWeighDTO {

    /**
     * 工单id
     */
    private Long ticketId;

    /**
     * 签名备注
     */
    private String remark;

    /**
     * 工单签名人
     */
    private String signUser;

}
