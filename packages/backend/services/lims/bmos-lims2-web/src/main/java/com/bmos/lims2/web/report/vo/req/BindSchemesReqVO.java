package com.bmos.lims2.web.report.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@ApiModel("绑定检验方案请求")
public class BindSchemesReqVO {

    @ApiModelProperty("方案ID集合（直接绑定方案）")
    @NotEmpty(message = "方案ID集合不能为空")
    private List<Long> schemeIds;
}
