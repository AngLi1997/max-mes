package com.bmos.mes.service.requisition.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("仓库批次领料-按物料件")
@Data
public class ReceiveRepositoryByMaterialDTO extends BusinessDataHandleBaseDTO {


    @ApiModelProperty(value = "货品批次主键id", required = true)
    @NotNull
    private Long materialBatchId;

    @ApiModelProperty(value = "领料单id", required = true)
    @NotNull
    private Long requisitionId;

    @ApiModelProperty(value = "货位id", required = true)
    @NotNull
    private Long cargoPositionId;

    @ApiModelProperty(value = "递交人id", required = true)
    @NotBlank
    private String senderId;

    @ApiModelProperty(value = "接收人id", required = true)
    @NotBlank
    private String receiverId;

    @ApiModelProperty(value = "勾选货品件id列表", required = true)
    @NotEmpty
    private List<Long> idList;

    /**
     * 打印机设备id
     */
    @ApiModelProperty(value = "打印机设备id", example = "1", required = true)
    private Long deviceId;

}
