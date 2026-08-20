package com.bmos.lims2.server.inspect.audit.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("样品审核分页查询参数")
public class SampleAuditPageQueryDTO extends BasePage {

    @ApiModelProperty("检验单号")
    private String orderNo;

    @ApiModelProperty("检品名称")
    private String materialName;

    @ApiModelProperty("检品编码")
    private String materialCode;

    @ApiModelProperty("检品ID集合")
    private List<Long> materialIds;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("请验时间起")
    private LocalDateTime inspectionRequestTimeStart;

    @ApiModelProperty("请验时间止")
    private LocalDateTime inspectionRequestTimeEnd;
}


