package com.bmos.mes.service.tag.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 扫描皮重标签
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/9/23 10:20
 */
@Data
@ApiModel("扫描皮重标签")
public class ScanTareWeighDTO {

    /**
     * 皮重配置id
     */
    @ApiModelProperty(value = "皮重配置id", example = "01", required = true)
    @NotNull
    private Long id;

}
