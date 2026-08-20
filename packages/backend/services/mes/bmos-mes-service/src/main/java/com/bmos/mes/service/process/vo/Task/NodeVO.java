package com.bmos.mes.service.process.vo.Task;

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
@ApiModel(value = "查询节点名称返回vo")
public class NodeVO {

    @ApiModelProperty("节点id")
    private Long id;

    @ApiModelProperty("节点名称")
    private String name;

    @ApiModelProperty("是否删除")
    private Boolean disabled;

    @ApiModelProperty("删除标识")
    private Boolean isDeleted;
}
