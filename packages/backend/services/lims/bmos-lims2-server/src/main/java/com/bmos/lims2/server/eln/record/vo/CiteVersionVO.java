package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "关联公式记录返回版本vo")
public class CiteVersionVO {

    @ApiModelProperty(value = "版本id")
    private Long versionId;

    @ApiModelProperty(value = "版本")
    private String version;
}
