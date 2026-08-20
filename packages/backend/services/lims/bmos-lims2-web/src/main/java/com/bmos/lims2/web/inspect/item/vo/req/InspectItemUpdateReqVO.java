package com.bmos.lims2.web.inspect.item.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ApiModel("编辑检验项目请求参数")
public class InspectItemUpdateReqVO {

    /**
     * 检品项id
     */
    @ApiModelProperty(value = "检验项id", required = true)
    @NotNull
    private Long id;

    /**
     * 检验项名称
     */
    @ApiModelProperty(value = "检验项名称", required = true)
    @Length(max = 30)
    @NotBlank
    private String name;


    /**
     * 描述
     */
    @ApiModelProperty(value = "描述")
    @Length(max = 100)
    private String remark;

    /**
     * 检品下的分析项
     */
    @ApiModelProperty(value = "检品下的分析项")
    private List<InspectItemParameterVO> parameterList;

}
