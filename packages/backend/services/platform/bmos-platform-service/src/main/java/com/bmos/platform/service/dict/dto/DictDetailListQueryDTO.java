package com.bmos.platform.service.dict.dto;

import com.bmos.mybatis.page.BasePage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "字典详情DTO")
public class DictDetailListQueryDTO extends BasePage {

    @ApiModelProperty(value = "字典id")
    @NotNull
    private Long dictId;

    @ApiModelProperty(value = "数据标签")
    private String dictLabel;

    @ApiModelProperty(value = "数据值")
    private String dictValue;
}
