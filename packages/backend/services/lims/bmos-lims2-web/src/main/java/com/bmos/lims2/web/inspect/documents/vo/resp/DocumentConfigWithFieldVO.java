package com.bmos.lims2.web.inspect.documents.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("请验单详情VO")
@Data
public class DocumentConfigWithFieldVO {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("请验单名称")
    private String name;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("请验单数据")
    private List<DocumentConfigFieldVO> dataList;

}
