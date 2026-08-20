package com.bmos.mes.service.process.dto;

import cn.hutool.core.util.StrUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jodd.util.StringUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author renjinguang
 */
@Getter
@Setter
@ToString
@ApiModel(value = "工艺版本发起审核dto")
public class ProcessVersionAuditDTO {

    @ApiModelProperty(value = "版本id",required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "生效时间,以年月日方式传递,立即生效时不传入时间")
    private String effectDate = StrUtil.DASHED;
}
