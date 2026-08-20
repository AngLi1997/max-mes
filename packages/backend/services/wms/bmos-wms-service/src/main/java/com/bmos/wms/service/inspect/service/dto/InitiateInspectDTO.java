package com.bmos.wms.service.inspect.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * WMS 发起请验 DTO（mirror MES InitiateInspectDTO，去掉生产相关字段）。
 *
 * <p>以库存批次id为入口：货品id、批号、平台物料id 均由后端从 {@code bw_inventory_batch} 反查，
 * 因为库存批次列表接口只返回批次id。分布式锁 key 在 controller 层用 {@code inventoryBatchId}。
 */
@Getter
@Setter
@ApiModel("WMS 发起请验DTO")
public class InitiateInspectDTO {

    @ApiModelProperty(value = "库存批次id（bw_inventory_batch.id）", required = true)
    @NotNull
    private Long inventoryBatchId;

    @ApiModelProperty(value = "请验单配置id（LIMS document_config.id）", required = true)
    @NotNull
    private Long inspectConfigId;

    @ApiModelProperty(value = "检验方案id", required = true)
    @NotNull
    private Long schemeId;

    @ApiModelProperty(value = "检验方案版本id", required = true)
    @NotNull
    private Long schemeVersionId;

    @ApiModelProperty(value = "请验单字段值", required = true)
    @NotNull
    private List<InitiateInspectInfoDTO> initiateInspectInfoDTOList;
}
