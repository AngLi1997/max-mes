package com.bmos.lims2.server.eln.signature.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 签名人信息查询
 * @author liang
 * @version 1.0.0
 * @date 2024/5/24 10:16
 */
@Data
@ToString
@ApiModel("双签名人信息查询(带工位)")
public class SignerQueryWithStationIdsDTO {

    /**
     * 权限码
     */
    @ApiModelProperty(value = "权限码")
    private Long permissionCode;

    /**
     * 工位id
     */
    @ApiModelProperty(value = "工位id")
    private List<Long> stationIds;
}
