package com.bmos.platform.service.dict.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@ApiModel(value = "字典查看返回vo")
public class DictWatchVO {

    @ApiModelProperty(value = "主键id")
    private Long id;

    @ApiModelProperty(value = "字典名称")
    private String dictName;

    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    @ApiModelProperty(value = "是否内置")
    private Boolean state;

    @ApiModelProperty(value = "字典数据集合")
    private List<DictDetailListVO> detailList;
}
