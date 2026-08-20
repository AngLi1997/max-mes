package com.bmos.mes.service.requisition.dto;

import com.bmos.mes.service.execute.dto.BusinessDataHandleBaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel("仓库批次量领料DTO")
@Data
public class ReceiveRepositoryByBatchDTO extends BusinessDataHandleBaseDTO{

    @ApiModelProperty(value = "勾选id列表", required = true)
    @NotEmpty
    private List<Long> idList;

    @ApiModelProperty(value = "领料单id", required = true)
    @NotNull
    private Long requisitionId;

    @ApiModelProperty(value = "货位id", required = true)
    @NotNull
    private Long cargoPositionId;

    /**
     * 递交人id
     */
    @ApiModelProperty(value = "递交人id", required = true)
    @NotBlank
    private String senderId;

    /**
     * 接收人id
     */
    @ApiModelProperty(value = "接收人id", required = true)
    @NotBlank
    private String receiverId;

    /**
     * 打印机设备id
     */
    @ApiModelProperty(value = "打印机设备id", example = "1", required = true)
    private Long deviceId;

}
