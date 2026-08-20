package com.bmos.platform.service.dict.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "查询字典列表dto")
public class DictListQueryDTO extends BasePage {

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典编码")
    private String dictCode;

}
