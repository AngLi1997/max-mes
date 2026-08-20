package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 暂存物料消耗相关参数
 * @author liang
 * @version 1.0.0
 * @date 2024/9/3 13:50
 */
@Data
@ApiModel("暂存物料消耗相关参数")
public class StorageMaterialConsumeDTO {

    /**
     * 暂存物料件id列表
     */
    @ApiModelProperty(value = "暂存物料件id列表", example = "1", required = true)
    @NotEmpty
    private List<Long> storageMaterialIdList;

    /**
     * 来源/去向
     */
    @ApiModelProperty(value = "来源/去向", example = "123", required = true)
    @NotBlank
    @Length(max = 200)
    private String linkExplain;

    /**
     * 操作人id
     */
    @ApiModelProperty(value = "操作人id", example = "1", required = true)
    @NotBlank
    private String operatorId;

    /**
     * 复核人id
     */
    @ApiModelProperty(value = "复核人", example = "1", required = true)
    @NotBlank
    private String reCheckerId;
}
