package com.bmos.lims2.web.inspect.parameter.vo.req;

import com.bmos.lims2.common.enums.AnalyzeResultTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 分析项保存请求VO
 */
@Getter
@Setter
@ApiModel("分析项保存请求VO")
public class InspectParameterSaveReqVO {

    /**
     * 分析项id
     */
    @ApiModelProperty("分析项id")
    private Long id;

    /**
     * 分析项名称
     */
    @ApiModelProperty(value = "分析项名称", required = true)
    @NotBlank(message = "分析项名称不能为空")
    @Length(max = 64, message = "分析项名称长度不能超过64个字符")
    private String name;

    /**
     * 分析项编码
     */
    @ApiModelProperty(value = "分析项编码", required = true)
    @NotBlank(message = "分析项编码不能为空")
    @Length(max = 64, message = "分析项编码长度不能超过64个字符")
    private String code;

    /**
     * 当前分析项默认标准规定
     */
    @ApiModelProperty("当前分析项默认标准规定")
    @Length(max = 255, message = "标准规定长度不能超过255个字符")
    private String standard;

    /**
     * 数据点列表
     */
    @ApiModelProperty(value = "数据点列表", required = true)
    @NotEmpty(message = "数据点列表不能为空")
    @Valid
    private List<InspectParameterDataPointReqVO> dataPoints;
} 