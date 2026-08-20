package com.bmos.lims2.web.inspect.documents.vo.req;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel("请验单配置分页查询VO")
@Data
public class DocumentConfigPageReqVO extends BasePage {

    @ApiModelProperty("请验单名称")
    private String name;

}
