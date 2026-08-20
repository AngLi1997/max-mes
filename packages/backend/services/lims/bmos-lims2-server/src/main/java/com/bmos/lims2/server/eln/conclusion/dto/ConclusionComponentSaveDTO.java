package com.bmos.lims2.server.eln.conclusion.dto;

import com.bmos.lims2.server.eln.entry.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * @Description: 结论组件保存DTO（前端仅提交是否符合规定的布尔值）
 * @Author: yigaohui
 * @Date: 2025/11/19 10:22
 */
@Getter
@Setter
@ApiModel("结论组件保存DTO")
public class ConclusionComponentSaveDTO extends BusinessDataHandleBaseDTO {

    @ApiModelProperty(value = "是否符合规定（true=符合，false=不符合）", required = true)
    @NotNull
    private Boolean compliant;

    @ApiModelProperty("备注")
    private String remark;
}

