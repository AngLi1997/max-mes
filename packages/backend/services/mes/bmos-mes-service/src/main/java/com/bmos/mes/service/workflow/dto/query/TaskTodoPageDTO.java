package com.bmos.mes.service.workflow.dto.query;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TaskTodoPageDTO extends BasePage {

    @ApiModelProperty("生产批号")
    private String batchNo;

    @ApiModelProperty("产品Id")
    private Long productId;

    @ApiModelProperty("班组权限")
    private List<Long> teamId;

    @ApiModelProperty("计划id")
    private Long planId;

}
