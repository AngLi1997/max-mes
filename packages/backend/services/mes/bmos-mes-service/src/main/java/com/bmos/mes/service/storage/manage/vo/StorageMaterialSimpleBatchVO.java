package com.bmos.mes.service.storage.manage.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author liang
 * @version 1.0.0
 * @date 2024/5/11 15:20
 */
@Data
@ApiModel("出库称量批次信息")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageMaterialSimpleBatchVO {

    /**
     * 物料批次id
     */
    @ApiModelProperty(value = "物料批次id", example = "1")
    private Long storageMaterialBatchId;

    /**
     * 物料批号
     */
    @ApiModelProperty(value = "物料批号", example = "001")
    private String storageMaterialBatchNo;

    /**
     * 有效期至
     */
    @ApiModelProperty(value = "有效期至", example = "2024-05-11")
    private LocalDate expiredDate;
}
