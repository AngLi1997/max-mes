package com.bmos.mes.service.process.dto.query;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel("工艺查询dto")
public class AuditOpinionQueryDTO extends BasePage {

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("工序名称")
    private String procedureName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("审批结论")
    private String confirmOpinion;
}
