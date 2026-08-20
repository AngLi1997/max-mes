package com.bmos.platform.service.log.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.logging.enums.OperationTypeEnum;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ApiModel("导出操作日志DTO")
public class ExportOperationLogDTO {
    @ApiModelProperty("已选择的id列表")
    private List<Long> selectIds;

    @ApiModelEnumProperty(value = "操作类型", enumClass = OperationTypeEnum.class)
    @EnumValidate(value = OperationTypeEnum.class)
    private Integer operationType;

    @ApiModelProperty("菜单id")
    private Long menuId;

    @ApiModelProperty("操作人")
    private String userName;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
