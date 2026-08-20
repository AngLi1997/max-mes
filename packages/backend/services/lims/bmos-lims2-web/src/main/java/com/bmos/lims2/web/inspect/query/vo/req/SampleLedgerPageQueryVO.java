package com.bmos.lims2.web.inspect.query.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 样品台账分页查询请求
 * @Author: yigaohui
 * @Date: 2025/09/05 11:40
 */
@Data
@ApiModel("样品台账分页查询请求")
public class SampleLedgerPageQueryVO extends BasePage {

    @ApiModelProperty("检品ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("检验单号")
    private String inspectionOrderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty(value = "样品状态（与返回status一致，可传1-6）：1-取样/2-接收/3-分样/4-领取/5-回收/6-处理；亦兼容 SAMPLED/RECEIVED/DIVIDED/COLLECTED/RECYCLED/PROCESSED 及中文，支持多选")
    private List<String> sampleStatuses;

    @ApiModelProperty("操作开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationStartTime;

    @ApiModelProperty("操作结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime operationEndTime;
}


