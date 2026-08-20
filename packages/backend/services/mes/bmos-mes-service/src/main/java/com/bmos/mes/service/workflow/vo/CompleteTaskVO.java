package com.bmos.mes.service.workflow.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @ClassName CompleteTaskVO
 * @Description TODO
 * @Author Ren Jin Guang
 * @Date 2024/11/7 14:08
 */
@Getter
@Setter
@ToString
public class CompleteTaskVO {

    @ApiModelProperty("是否完成任务")
    private Boolean completeTask;

    @ApiModelProperty("是否进行了开启了暂停的工序")
    private Boolean startPauseProcedure;
}
