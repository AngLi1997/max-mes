package com.bmos.mes.service.inspect.controller.vo;

import com.bmos.mes.common.enums.inpspect.InspectProgramResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("检验项结果")
public class InspectProgramResultVO {

    /**
     * 检验项id
     */
    @ApiModelProperty("检验项id")
    private Long id;

    /**
     * 检验项代码
     */
    @ApiModelProperty("检验项代码")
    private String inspectProgramNo;
    /**
     * 检验项名称
     */
    @ApiModelProperty("检验项名称")
    private String inspectProgramName;
    /**
     * 检验项结果
     */
    @ApiModelProperty("检验项结果")
    private String inspectResult;
    /**
     * 检验结论
     */
    @ApiModelProperty("检验结论")
    private InspectProgramResultEnum inspectConclusion;

}
