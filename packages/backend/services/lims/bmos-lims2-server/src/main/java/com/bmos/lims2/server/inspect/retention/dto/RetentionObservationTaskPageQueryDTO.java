package com.bmos.lims2.server.inspect.retention.dto;

import com.bmos.mybatis.page.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @Description: 留样观察任务分页查询DTO
 * @Author: yigaohui
 * @Date: 2026/02/06
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RetentionObservationTaskPageQueryDTO extends BasePage {

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
     * 查询类型：upcoming-临期任务（一周内到期），all-全部
     */
    private String queryType;
}
