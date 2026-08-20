package com.bmos.lims2.web.inspect.documents.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@ApiModel("请验单绑定检品VO")
public class DocumentConfigBindProductVO {

    @ApiModelProperty("请验单id")
    @NotNull
    private Long id;

    @ApiModelProperty("检品id列表")
    private List<Long> materialIdList;

}
