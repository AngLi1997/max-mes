package com.bmos.platform.service.factory.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel("导出操作日志入参")
public class RoomStatusLogExportDTO extends RoomLogPageDTO {

    /**
     * 是否导出
     */
    @ApiModelProperty("是否全部导出")
    private Boolean all;

}
