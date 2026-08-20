package com.bmos.wms.service.sendout.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 发料DTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/4/15 22:44
 */
@Data
@ApiModel("发料DTO")
public class SendOutDTO {

    /**
     * 发料工单id
     */
    @ApiModelProperty(value = "发料工单id", example = "1")
    private Long id;

    /**
     * 发料明细
     */
    @ApiModelProperty(value = "发料明细")
    @NotEmpty
    private List<SendOutItemDTO> sendList;

    /**
     * 发料人id
     */
    @ApiModelProperty(value = "发料人id", example = "1", required = true)
    @NotBlank
    private String senderId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    @NotBlank
    private String reCheckerId;

    /**
     * 发料库存
     */
    @Data
    public static final class SendOutItemDTO {

        @ApiModelProperty(value = "货品id或批次id")
        private Long businessId;

        @ApiModelProperty(value = "货品件id列表")
        private List<Long> inventoryIds;
    }
}
