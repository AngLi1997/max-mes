package com.bmos.lims2.web.report.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ApiModel("报告生成开始参数")
public class ReportGenerateStartReqVO {

    @ApiModelProperty("模板版本ID")
    @NotNull
    private Long templateVersionId;

    @ApiModelProperty("检验单ID")
    @NotNull
    private Long inspectionOrderId;

    @ApiModelProperty("选中的操作规程版本ID列表（检验依据）")
    private List<Long> selectedOperateVersionIds;
}


