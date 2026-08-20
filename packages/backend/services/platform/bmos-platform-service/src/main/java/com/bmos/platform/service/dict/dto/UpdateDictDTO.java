package com.bmos.platform.service.dict.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "编辑字典DTO")
public class UpdateDictDTO {

    @ApiModelProperty(value = "字典id")
    @NotNull
    private Long id;

    @ApiModelProperty(value = "字典名称")
    @NotBlank
    private String dictName;

    @ApiModelProperty(value = "字典编码")
    @NotBlank
    private String dictCode;

    @ApiModelProperty(value = "需要删除的id集合")
    private List<Long> dictIdList;

    @ApiModelProperty(value = "字典详情数据")
    private List<UpdateDetailDTO> detailList;
}
