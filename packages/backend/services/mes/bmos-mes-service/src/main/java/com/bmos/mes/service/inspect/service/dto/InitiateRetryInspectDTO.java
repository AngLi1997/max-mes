package com.bmos.mes.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 发起请验DTO
 */
@Getter
@Setter
@ApiModel(value = "重新发起请验DTO")
public class InitiateRetryInspectDTO {

    /**
     * 请验单id
     */
    @ApiModelProperty(value = "请验单id", required = true)
    @NotNull
    private Long id;

    /**
     * 配方物料id
     */
    @ApiModelProperty(value = "配方物料id", required = true)
    @NotNull
    private Long formulaMaterialId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "所需要请验的物料批号", required = true)
    @NotNull
    private String materialBatchNo;


    @ApiModelProperty(value = "请验单号")
    @NotNull
    private String inspectNo;

    /**
     * 请验单配置id
     */
    @ApiModelProperty(value = "请验单配置id", required = true)
    @NotNull
    private Long inspectConfigId;

    /**
     * 请验单信息
     */
    @ApiModelProperty(value = "请验单信息")
    private List<InitiateInspectInfoDTO> initiateInspectInfoDTOList;

    /**
     * 检验方案id（自研LIMS路径）
     */
    @ApiModelProperty("检验方案id（自研LIMS路径）")
    private Long schemeId;

    /**
     * 检验方案版本id（自研LIMS路径）
     */
    @ApiModelProperty("检验方案版本id（自研LIMS路径）")
    private Long schemeVersionId;

}
