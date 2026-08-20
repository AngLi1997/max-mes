package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("记录项单个保存VO")
@Data
public class SaveSingleItemVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("itemId")
    private Long itemId;

    @ApiModelProperty("名称")
    private String name;

}
