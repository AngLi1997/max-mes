package com.bmos.mes.service.process.dto.query;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@ApiModel("查询工艺列表dto")
public class ProcedureConfirmQueryDTO extends BasePage {

    @ApiModelProperty("工艺结论id")
    @NotNull
    private Long processConfirmId;
}
