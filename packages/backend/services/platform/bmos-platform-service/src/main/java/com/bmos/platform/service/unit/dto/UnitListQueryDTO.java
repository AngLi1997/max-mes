package com.bmos.platform.service.unit.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@ApiModel(value = "查询列表集合dto")
public class UnitListQueryDTO extends BasePage {

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("标准单位id")
    private Long id;
}
