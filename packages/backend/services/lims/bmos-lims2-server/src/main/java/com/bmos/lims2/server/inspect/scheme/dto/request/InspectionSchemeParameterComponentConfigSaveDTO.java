package com.bmos.lims2.server.inspect.scheme.dto.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@ApiModel("工序步骤记录项配置保存DTO")
public class InspectionSchemeParameterComponentConfigSaveDTO {

    @ApiModelProperty("分析项id")
    @NotNull
    private Long parameterId;

    @ApiModelProperty("分析项配置id")
    private Long parameterConfigId;


    @ApiModelProperty("工艺id")
    @NotNull
    private Long schemeId;

    @ApiModelProperty("工艺版本")
    @NotNull
    private Long schemeVersionId;

    /**
     * 记录项id
     */
    @ApiModelProperty("记录项id")
    @NotNull
    private Long recordItemId;

    @ApiModelProperty("记录项版本id")
    private Long recordVersionId;

    @ApiModelProperty("组件信息")
    private List<ComponentConfigDTO> components;
}
