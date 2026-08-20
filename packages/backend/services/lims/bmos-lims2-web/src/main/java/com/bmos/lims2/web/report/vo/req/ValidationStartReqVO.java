package com.bmos.lims2.web.report.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("报告验证开始请求VO")
public class ValidationStartReqVO {

    @ApiModelProperty("模板版本ID")
    @NotNull
    private Long templateVersionId;

    @ApiModelProperty("检品ID")
    @NotNull
    private Long materialId;

    @ApiModelProperty("检验单ID（样品审核已通过）")
    @NotNull
    private Long inspectionOrderId;

    @ApiModelProperty("选中的操作规程版本ID列表（检验依据）")
    private List<Long> selectedOperateVersionIds;
}


