package com.bmos.mes.service.storage.manage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 物料件组件保存参数
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:51
 */
@Data
@ApiModel("物料件组件保存参数")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StorageMaterialComponentDTO {

    /**
     * 物料件组件实例id
     */
    @ApiModelProperty(value = "物料件组件实例id", example = "1", required = true)
    @NotNull
    private Long componentInstanceId;

    /**
     * 物料件号
     */
    @ApiModelProperty(value = "物料件号", example = "1", required = true)
    @NotBlank
    private String no;
}
