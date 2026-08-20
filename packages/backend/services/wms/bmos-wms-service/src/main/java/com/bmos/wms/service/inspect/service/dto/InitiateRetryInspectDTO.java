package com.bmos.wms.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * WMS 重新发起请验 DTO（mirror MES Plan B：作废原单 + 新建）。
 *
 * <p>批次 / 货品 / 平台物料id 全部从原检验单（{@code id} 指向的 bw_inspect）反查，前端无需重复传。
 */
@Getter
@Setter
@ApiModel("WMS 重新发起请验DTO")
public class InitiateRetryInspectDTO {

    @ApiModelProperty(value = "原 inspect.id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "请验单配置id", required = true)
    @NotNull
    private Long inspectConfigId;

    @ApiModelProperty(value = "检验方案id", required = true)
    @NotNull
    private Long schemeId;

    @ApiModelProperty(value = "检验方案版本id", required = true)
    @NotNull
    private Long schemeVersionId;

    @ApiModelProperty("请验单字段（如需修改，否则沿用原单）")
    private List<InitiateInspectInfoDTO> initiateInspectInfoDTOList;

    @ApiModelProperty("重新发起原因")
    private String reason;
}
