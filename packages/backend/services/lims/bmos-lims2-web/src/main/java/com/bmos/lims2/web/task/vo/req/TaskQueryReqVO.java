package com.bmos.lims2.web.task.vo.req;

import com.bmos.lims2.common.enums.TaskStatusEnum;
import com.bmos.mybatis.page.BasePage;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 任务查询请求VO
 * 
 * @author system
 * @since 2025/01/29
 */
@Getter
@Setter
@ApiModel("任务查询请求")
public class TaskQueryReqVO extends BasePage {


    @ApiModelProperty("检品Id集合")
    private List<Long> materialIds;

    @ApiModelProperty("任务状态")
    private TaskStatusEnum status;

    @ApiModelProperty("检验单编号")
    private String inspectionOrderNo;

    @ApiModelProperty("批次号")
    private String batchNo;

    @ApiModelProperty("分析项名称")
    private String parameterName;

    @ApiModelProperty("检验项目名称")
    private String inspectItemName;

    @ApiModelProperty("请验时间开始")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTimeStart;

    @ApiModelProperty("请验时间结束")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestTimeEnd;
}
