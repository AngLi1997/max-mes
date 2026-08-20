package com.bmos.wms.service.storage.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 创建存储区域DTO
 *
 * @author liang
 * @version 1.0.0
 * @date 2024/2/5 11:32
 */
@Data
@ApiModel("创建存储区域DTO")
public class StorageCreateDTO {

    /**
     * 上级区域id
     */
    @ApiModelProperty("上级区域id")
    private Long parentId;

    /**
     * 区域名称
     */
    @ApiModelProperty(value = "区域名称", required = true)
    @NotBlank
    @Length(max = 100)
    private String name;
}
