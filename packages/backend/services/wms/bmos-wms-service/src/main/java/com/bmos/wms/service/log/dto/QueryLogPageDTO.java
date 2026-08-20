package com.bmos.wms.service.log.dto;

import com.bmos.common.validate.EnumValidate;
import com.bmos.logging.enums.OperationTypeEnum;
import com.bmos.mybatis.page.BasePage;
import com.bmos.web.swagger.base.ApiModelEnumProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("日志分页查询DTO")
public class QueryLogPageDTO extends BasePage {

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

}
