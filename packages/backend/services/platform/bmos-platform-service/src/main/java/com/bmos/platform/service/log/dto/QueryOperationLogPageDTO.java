package com.bmos.platform.service.log.dto;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.bmos.common.validate.EnumValidate;
import com.bmos.logging.enums.OperationTypeEnum;
import com.bmos.mybatis.page.BasePage;
import com.bmos.platform.common.GlobalConstants;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@ApiModel("日志分页查询DTO")
public class QueryOperationLogPageDTO extends BasePage {

    @ApiModelProperty
    private Long menuId;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("操作人")
    private String userName;

    @ApiModelEnumProperty(value = "操作类型", enumClass = OperationTypeEnum.class)
    @EnumValidate(value = OperationTypeEnum.class)
    private Integer operationType;

    private LocalDateTime startTimeDate;

    private LocalDateTime endTimeDate;

    public void convert2Date(){
        if (StrUtil.isNotEmpty(startTime) && StrUtil.isNotEmpty(endTime)){
            setStartTimeDate(LocalDateTimeUtil.parse(startTime + " 00:00:00", GlobalConstants.DATE_TIME_FORMAT));
            setEndTimeDate(LocalDateTimeUtil.parse(endTime + " 23:59:59", GlobalConstants.DATE_TIME_FORMAT));
        }
    }



}
