package com.bmos.lims2.server.inspect.sample.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 样品处理分页查询参数
 * @Author: yigaohui
 * @Date: 2025/02/01 10:30
 */
@Getter
@Setter
@ApiModel("样品处理分页查询参数")
public class SampleHandlePageQueryDTO extends BasePage {

    @ApiModelProperty("物料ID列表（来自左侧物料树）")
    private List<Long> materialIds;

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("批号")
    private String batchNo;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("检验结束时间-开始")
    private LocalDateTime endTimeStart;

    @ApiModelProperty("检验结束时间-结束")
    private LocalDateTime endTimeEnd;

    @ApiModelProperty(value = "状态：0-全部，1-待回收，2-待处理，3-已处理")
    private Integer status;
}


