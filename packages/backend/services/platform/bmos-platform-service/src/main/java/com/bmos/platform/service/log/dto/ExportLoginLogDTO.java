package com.bmos.platform.service.log.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("导出登录日志DTO")
public class ExportLoginLogDTO {
    @ApiModelProperty("已选择的id列表")
    private List<Long> selectIds;

    @ApiModelProperty("账号")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
