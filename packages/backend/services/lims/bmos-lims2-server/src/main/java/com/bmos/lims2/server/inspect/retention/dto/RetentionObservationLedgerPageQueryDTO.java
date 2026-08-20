package com.bmos.lims2.server.inspect.retention.dto;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * @Description: 留样观察台账分页查询DTO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RetentionObservationLedgerPageQueryDTO extends BasePage {

    /**
     * 物料ID集合
     */
    private List<Long> materialIds;

    /**
     * 批号
     */
    private String batchNo;

    /**
     * 样品编号
     */
    private String sampleNo;

    /**
     * 观察开始日期
     */
    private LocalDate observationStartDate;

    /**
     * 观察结束日期
     */
    private LocalDate observationEndDate;
}
