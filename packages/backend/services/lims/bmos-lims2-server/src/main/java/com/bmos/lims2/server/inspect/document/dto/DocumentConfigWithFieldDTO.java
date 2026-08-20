package com.bmos.lims2.server.inspect.document.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("请验单详情DTO")
@Data
public class DocumentConfigWithFieldDTO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("请验单名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;


    @ApiModelProperty("启停状态")
    private Boolean status;

    @ApiModelProperty("请验单数据")
    private List<DocumentConfigFieldDTO> dataList;

}
