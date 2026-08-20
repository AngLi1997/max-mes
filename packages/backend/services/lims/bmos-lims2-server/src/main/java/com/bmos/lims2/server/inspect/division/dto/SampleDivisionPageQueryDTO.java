package com.bmos.lims2.server.inspect.division.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description: 分样列表查询DTO
 * @Author: yigaohui
 * @Date: 2025/01/29 16:45
 */
@Getter
@Setter
@Accessors(chain = true)
@ApiModel("分样列表查询数据对象")
public class SampleDivisionPageQueryDTO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("请验开始时间")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("请验结束时间")
    private LocalDateTime inspectionRequestTimeEnd;

    @ApiModelProperty("样品编号")
    private String sampleNo;

    @ApiModelProperty("样品名称")
    private String sampleName;

    @ApiModelProperty("检品ID集合")
    private List<Long> materialIds;
}