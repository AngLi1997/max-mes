package com.bmos.adaptor.active;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RsaVO {
    @ApiModelProperty("激活状态")
    private Boolean active;

    @ApiModelProperty("日期 ALL代表永久激活  时间格式 yyyy-MM-dd HH:mm:ss 未激活为null")
    private String date;
}
