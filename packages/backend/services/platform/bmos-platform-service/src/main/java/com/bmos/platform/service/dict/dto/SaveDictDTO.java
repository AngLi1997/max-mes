package com.bmos.platform.service.dict.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "添加字典DTO")
public class SaveDictDTO {

    @ApiModelProperty("字典名称")
    @NotNull
    @Length(max = 100)
    private String dictName;

    @ApiModelProperty("字典编码")
    @NotNull
    private String dictCode;

    @ApiModelProperty("字典数据集合")
    private List<SaveDictDetailDTO> detailList;

}
