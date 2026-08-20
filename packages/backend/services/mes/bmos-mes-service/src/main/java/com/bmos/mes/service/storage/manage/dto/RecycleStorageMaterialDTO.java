package com.bmos.mes.service.storage.manage.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@ApiModel("回收物料DTO")
@Data
public class RecycleStorageMaterialDTO extends BusinessDataHandleBaseDTO {

    @ApiModelProperty("批次id")
    @NotNull
    private Long materialBatchId;

    @ApiModelProperty("回收量")
    @NotNull
    private BigDecimal quantity;

    @ApiModelProperty("操作人id")
    @NotBlank
    private String operatorId;

    @ApiModelProperty("单位id")
    @NotNull
    private Long unitId;

    @ApiModelProperty("容器id/设备id")
    private Long deviceId;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("投料回收主键id(非componentId)")
    @NotNull
    private Long chargeRecycleComponentId;

    @ApiModelProperty(value = "产品id",hidden = true)
    private Long productId;

    @ApiModelProperty(hidden = true)
    private String deviceName;

    @ApiModelProperty(hidden = true)
    private String deviceCode;

    @ApiModelProperty("打印机id")
    private Long printerId;


}
