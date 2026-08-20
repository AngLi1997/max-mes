package com.bmos.platform.facade.equipment.dto;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiOperation("修改工位使用状态dto")
public class UpdateStationDTO {

    @ApiModelProperty("工位id集合")
    @NotEmpty
    private List<Long> stationIdList;

    @ApiModelProperty("操作类型: true:绑定，false:解绑")
    private Boolean type;
}
