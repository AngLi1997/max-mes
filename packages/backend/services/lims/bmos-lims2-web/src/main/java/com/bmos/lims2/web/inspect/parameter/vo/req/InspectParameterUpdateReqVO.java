package com.bmos.lims2.web.inspect.parameter.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 分析项更新请求VO
 */
@Getter
@Setter
@ApiModel("分析项更新请求VO")
public class InspectParameterUpdateReqVO extends InspectParameterSaveReqVO {
}
