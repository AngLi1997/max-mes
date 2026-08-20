package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 暂存物料批次移库参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("暂存物料批次移库参数")
public class StorageMaterialMoveDTO {

    /**
     * 暂存物料批次id
     */
    @ApiModelProperty(value = "暂存物料批次id", example = "1", required = true)
    @NotNull
    private Long storageMaterialBatchId;

    /**
     * 暂存物料id列表
     */
    @ApiModelProperty(value = "暂存物料id列表", required = true)
    @NotEmpty
    private List<Long> storageMaterialIdList;

    /**
     * 移入货位
     */
    @ApiModelProperty(value = "移入货位", example = "1", required = true)
    @NotNull
    private Long targetMaterialPositionId;

    /**
     * 来源/去向
     */
    @ApiModelProperty(value = "来源/去向", example = "123", required = true)
    @NotBlank
    @Length(max = 200)
    private String linkExplain;

    /**
     * 移库人id
     */
    @ApiModelProperty(value = "移库人id", example = "1", required = true)
    @NotBlank
    private String moverId;
}
