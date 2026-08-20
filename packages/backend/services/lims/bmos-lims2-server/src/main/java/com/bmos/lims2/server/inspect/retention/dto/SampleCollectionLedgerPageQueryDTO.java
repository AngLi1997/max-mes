package com.bmos.lims2.server.inspect.retention.dto;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * @Description: 留样领用台账分页查询DTO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SampleCollectionLedgerPageQueryDTO extends BasePage {

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
     * 领用开始日期
     */
    private LocalDate collectStartDate;

    /**
     * 领用结束日期
     */
    private LocalDate collectEndDate;
}
