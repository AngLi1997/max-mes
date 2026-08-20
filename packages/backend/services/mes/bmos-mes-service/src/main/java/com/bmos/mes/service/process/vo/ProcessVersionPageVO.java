package com.bmos.mes.service.process.vo;

import com.bmos.common.util.enums.EnumUtils;
import com.bmos.mes.common.enums.process.ActionStateEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel("版本分页查询VO")
public class ProcessVersionPageVO {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("工艺名称")
    private String name;

    @ApiModelProperty("工艺id")
    private Long processId;

    @ApiModelProperty("版本号")
    private String version;

    @ApiModelProperty("配方版本id")
    private Long productFormulaVersionId;

    @ApiModelProperty("配方版本号")
    private String productFormulaVersionNo;

    @ApiModelProperty("配方名称")
    private String productFormulaName;

    @ApiModelProperty("流程模型id")
    private String processModelId;

    @ApiModelProperty("操作状态名称")
    private String actionStateName;

    @ApiModelProperty("操作状态值")
    private ActionStateEnum actionState;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("启停状态")
    private Boolean state;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

    @ApiModelProperty("生效时间")
    private String effectDate;

}
