package com.bmos.lims2.web.inspect.sample.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("样品处理分页查询请求")
public class SampleHandlePageReqVO extends BasePage {

    @ApiModelProperty("物料分类ID（仅传分类时由后端解析启用物料ID集；为空则不按分类过滤）")
    private Long categoryId;

    @ApiModelProperty("物料ID（与分类同时传时以物料ID优先）")
    private Long materialId;

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


