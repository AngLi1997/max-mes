package com.bmos.lims2.server.stability.plan.dto.request;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 稳定性时间点取样列表分页查询DTO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StabilityTimepointSampleQueryDTO extends BasePage {

    /** 样品编号（子样品，模糊查询） */
    private String sampleNo;

    /** 批号（模糊查询） */
    private String batchNo;

    /** 稳定性考察编号（模糊查询） */
    private String planCode;

    /** 检验单号（模糊查询） */
    private String orderNo;

    /** 请验时间起（含） */
    private LocalDateTime requestTimeStart;

    /** 请验时间止（含） */
    private LocalDateTime requestTimeEnd;
}
