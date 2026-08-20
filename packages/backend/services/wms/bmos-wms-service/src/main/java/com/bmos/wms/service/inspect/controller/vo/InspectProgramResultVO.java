package com.bmos.wms.service.inspect.controller.vo;

import com.bmos.wms.common.enums.inspect.InspectProgramResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("检验项结果VO")
public class InspectProgramResultVO {

    @ApiModelProperty("检验项id")
    private Long id;

    @ApiModelProperty("检验项代码")
    private String inspectProgramNo;

    @ApiModelProperty("检验项名称")
    private String inspectProgramName;

    @ApiModelProperty("检验项结果")
    private String inspectResult;

    @ApiModelProperty("检验结论")
    private InspectProgramResultEnum inspectConclusion;
}
