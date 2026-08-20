package com.bmos.platform.service.signature.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.common.GlobalConstants;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("签名分页查询dto")
public class SignatureQueryPageDTO extends BasePage {

    @ApiModelProperty("系统编码")
    private Integer systemCode;

    @ApiModelProperty("签名人")
    private String userName;

    @ApiModelProperty("开始时间")
    @NotEmpty
    private String startTime;

    @ApiModelProperty("结束时间")
    @NotEmpty
    private String endTime;

    private LocalDateTime startTimeDate;

    private LocalDateTime endTimeDate;

    public void convert2Date(){
        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)){
            setStartTimeDate(LocalDateTimeUtil.parse(startTime + " 00:00:00", GlobalConstants.DATE_TIME_FORMAT));
            setEndTimeDate(LocalDateTimeUtil.parse(endTime + " 23:59:59", GlobalConstants.DATE_TIME_FORMAT));
        }
    }
}
