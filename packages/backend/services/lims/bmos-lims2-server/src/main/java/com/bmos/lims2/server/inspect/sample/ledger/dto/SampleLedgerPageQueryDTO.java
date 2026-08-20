package com.bmos.lims2.server.inspect.sample.ledger.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 样品台账分页查询参数
 * @Author: yigaohui
 * @Date: 2025/09/05 11:20
 */
@Data
@ApiModel("样品台账分页查询参数")
public class SampleLedgerPageQueryDTO extends BasePage {

    @ApiModelProperty("检品ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("检验单号")
    private String inspectionOrderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty(value = "样品状态：SAMPLED/RECEIVED/COLLECTED/RECYCLED/PROCESSED/DIVIDED，支持多选")
    private List<String> sampleStatuses;

    @ApiModelProperty("操作开始时间")
    private LocalDateTime operationStartTime;

    @ApiModelProperty("操作结束时间")
    private LocalDateTime operationEndTime;
}


