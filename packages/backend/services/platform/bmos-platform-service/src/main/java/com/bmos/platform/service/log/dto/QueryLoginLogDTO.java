package com.bmos.platform.service.log.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.platform.common.utils.TimeUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("查询分页DTO")
public class QueryLoginLogDTO extends BasePage {

    @ApiModelProperty("账号")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("开始时间")
    @NotEmpty
    private String startTime;

    @ApiModelProperty("结束时间")
    @NotEmpty
    private String endTime;

    private LocalDateTime startTimeDate;

    private LocalDateTime endTimeDate;

    public void convert2Data(){
        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)){
            setStartTimeDate(LocalDateTimeUtil.parse(startTime + " 00:00:00", GlobalConstants.DATE_TIME_FORMAT));
            setEndTimeDate(LocalDateTimeUtil.parse(endTime + " 23:59:59", GlobalConstants.DATE_TIME_FORMAT));
        }
    }

}
