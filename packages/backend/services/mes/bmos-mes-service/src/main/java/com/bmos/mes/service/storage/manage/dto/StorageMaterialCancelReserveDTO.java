package com.bmos.mes.service.storage.manage.dto;

import com.bmos.mes.common.enums.storage.StorageOperateTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 取消物料预定参数（移动端）
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/3/13 14:43
 */
@Data
@ApiModel("取消物料预定参数(移动端)")
public class StorageMaterialCancelReserveDTO {

    /**
     * 产品id
     */
    @ApiModelProperty(value = "产品id", example = "1", required = true)
    @NotNull
    private Long productId;

    /**
     * 工艺id
     */
    @ApiModelProperty(value = "工艺id", example = "1", required = true)
    @NotNull
    private Long processId;

    /**
     * 生产批次id
     */
    @ApiModelProperty(value = "生产批次id", example = "1", required = true)
    @NotNull
    private Long batchId;

    /**
     * 暂存物料id列表
     */
    @ApiModelProperty(value = "暂存物料id列表", required = true)
    @NotEmpty
    private List<Long> storageMaterialIdList;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注", example = "备注", required = true)
    @NotBlank
    @Length(max = 200)
    private String remark;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id", example = "1", required = true)
    @NotBlank
    private String operatorId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人id", example = "1", required = true)
    @NotBlank
    private String reCheckerId;

    @ApiModelProperty(hidden = true)
    private StorageOperateTypeEnum operateType;
}
