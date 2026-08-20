package com.bmos.lims2.web.inspect.retention.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;

/**
 * @Description: 留样领用台账分页查询请求VO
 * @Author: yigaohui
 * @Date: 2026/02/10
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel("留样领用台账分页查询请求")
public class SampleCollectionLedgerPageReqVO extends BasePage {

    @ApiModelProperty("物料ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("领用开始日期")
    private LocalDate collectStartDate;

    @ApiModelProperty("领用结束日期")
    private LocalDate collectEndDate;
}
