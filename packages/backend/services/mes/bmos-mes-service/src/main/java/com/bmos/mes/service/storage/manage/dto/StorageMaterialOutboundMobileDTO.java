package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 暂存物料件出库参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("暂存物料件出库参数(移动端)")
public class StorageMaterialOutboundMobileDTO {

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1", required = true)
    private Long materialPositionId;

    /**
     * 出库信息列表
     */
    @ApiModelProperty(value = "出库信息列表", required = true)
    @NotEmpty
    @Valid
    private List<OutBoundDTO> outboundList;

    /**
     * 来源/去向
     */
    @ApiModelProperty(value = "来源/去向", example = "123", required = true)
    @NotBlank
    @Length(max = 200)
    private String linkExplain;

    /**
     * 递交人id
     */
    @ApiModelProperty(value = "递交人id", example = "1", required = true)
    @NotBlank
    private String senderId;

    /**
     * 接收人id
     */
    @ApiModelProperty(value = "接收人id", example = "1", required = true)
    @NotBlank
    private String receiverId;

    /**
     * 出库信息
     */
    @Data
    @ApiModel("暂存物料批次出库信息")
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OutBoundDTO {

        /**
         * 物料件id
         */
        @ApiModelProperty("暂存物料件id")
        @NotNull
        private Long id;
    }
}
