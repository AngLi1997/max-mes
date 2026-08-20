package com.bmos.platform.facade.dict.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 字典相关feignVO
 */
@Getter
@Setter
public class DictDetailFeignVO {

    /**
     * 字典id
     */
    @ApiModelProperty(value = "字典id")
    private Long id;

    /**
     * 字典名称
     */
    @ApiModelProperty(value = "字典名称")
    private String dictName;

    /**
     * 字典编码
     */
    @ApiModelProperty(value = "字典编码")
    private String dictCode;

    /**
     * 字典数据
     */
    @ApiModelProperty(value = "字典数据")
    private List<DictDataFeignVO> dictDataList;
}
