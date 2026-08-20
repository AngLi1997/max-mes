package com.bmos.mes.service.formula.vo;

import com.bmos.mes.common.enums.formula.FormulaVersionStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
@Data
@ApiModel("配方版本分页VO")
public class ProductFormulaVersionPageVO {

    @ApiModelProperty("版本id")
    private Long id;

    @ApiModelProperty("版本号")
    private String versionNo;

    @ApiModelProperty("版本描述")
    private String description;

    @ApiModelProperty("状态")
    private FormulaVersionStatusEnum status;

    @ApiModelProperty("启停")
    private Boolean enable;

    @ApiModelProperty("流程实例id")
    private String processInstanceId;

}
