package com.bmos.lims2.web.inspect.item.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@ApiModel("新增检验项目请求参数")
public class InspectItemCreateReqVO {

    /**
     * 检验项名称
     */
    @ApiModelProperty(value = "检验项名称", required = true)
    @Length(max = 30)
    @NotNull
    private String name;

    /**
     * 检验项编码
     */
    @ApiModelProperty(value = "检验项编码", required = true)
    @Length(max = 30)
    @NotNull
    private String code;


    /**
     * 描述
     */
    @ApiModelProperty(value = "备注")
    @Length(max = 100)
    private String remark;

    /**
     * 当前检验项下的分析项
     */
    @ApiModelProperty(value = "当前检验项下的分析项", required = true)
    @NotNull
    private List<InspectItemParameterVO> parameterList;

}
