package com.bmos.lims2.server.eln.record.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author renjinguang
 */
@Setter
@Getter
@ToString
@ApiModel(value = "引用记录返回vo")
public class CiteRecordVO {

    @ApiModelProperty(value = "记录名称")
    private String name;

    @ApiModelProperty(value = "记录id")
    private Long id;

    @ApiModelProperty(value = "版本集合")
    private List<CiteVersionVO> versionList;
}
