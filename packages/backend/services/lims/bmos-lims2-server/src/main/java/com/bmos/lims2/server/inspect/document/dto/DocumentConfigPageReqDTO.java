package com.bmos.lims2.server.inspect.document.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("请验单配置分页DTO")
@Data
public class DocumentConfigPageReqDTO extends BasePage {

    @ApiModelProperty("请验单名称")
    private String name;

}
