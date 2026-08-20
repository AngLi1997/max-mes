package com.bmos.lims2.server.inspect.document.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 请验单配置字段排序更新DTO
 * @Author: yigaohui
 * @Date: 2025/09/30 11:30
 */
@Data
@ApiModel("请验单配置字段排序更新DTO")
public class DocumentConfigFieldSortUpdateDTO {

    @ApiModelProperty(value = "请验单配置ID", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "字段排序列表", required = true)
    private List<FieldSortDTO> sorts;

}

