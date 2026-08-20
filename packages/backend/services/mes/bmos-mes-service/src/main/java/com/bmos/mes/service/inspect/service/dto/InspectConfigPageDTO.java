package com.bmos.mes.service.inspect.service.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("请验单配置分页请求参数")
public class InspectConfigPageDTO extends BasePage {

    /**
     * 请验单名称
     */
    @ApiModelProperty("请验单名称")
    private String name;

}
