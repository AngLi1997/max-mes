package com.bmos.mes.service.lotrelease.template.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 批签发模板绑定工艺DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/8/19 10:46
 */
@ApiModel("批签发模板绑定工艺DTO")
@Data
public class LotReleaseTemplateBindProcessDTO {

    @ApiModelProperty(value = "批签发模板id", example = "1")
    private Long lotReleaseTemplateId;

    @ApiModelProperty(value = "工艺id列表", example = "1")
    private List<Long> processIds = new ArrayList<>();
}
