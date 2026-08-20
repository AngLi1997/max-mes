package com.bmos.mes.service.weigh.centre.config.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 称量中心绑定工位DTO
 * @author liang
 * @version 1.0.0
 * @date 2024/7/3 17:09
 */
@Data
@ApiModel("称量中心绑定工位DTO")
public class WeighCentreBindStationDTO {

    @ApiModelProperty(value = "称量中心id", example = "1", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "工位id列表", required = true)
    private List<Long> stationIdList;
}
