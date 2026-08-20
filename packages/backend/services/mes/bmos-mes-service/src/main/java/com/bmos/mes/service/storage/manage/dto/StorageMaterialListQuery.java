package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * 暂存物料件分页查询参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("暂存物料件列表查询参数")
public class StorageMaterialListQuery {

    /**
     * 暂存物料批次id
     */
    @ApiModelProperty(value = "暂存物料批次id", example = "1", required = true)
    @NotNull
    private Long storageMaterialBatchId;

    /**
     * 货位id
     */
    @ApiModelProperty(value = "货位id", example = "1")
    private Long materialPositionId;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "000000002")
    @Length(max = 100)
    private String materialNo;
}
